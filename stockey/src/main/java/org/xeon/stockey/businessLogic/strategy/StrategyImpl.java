package org.xeon.stockey.businessLogic.strategy;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.omg.SendingContext.RunTime;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.xeon.stockey.businessLogicService.strategyService.StrategyService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;

/**
 * 策略模块接口StrategyService的实现
 * Created by Sissel on 2016/5/28.
 */

@RestController
@RequestMapping("/strategy/trans")
public class StrategyImpl implements StrategyService {
    private static final String tmpPath = StrategyImpl.class.getResource("tmpFiles").getPath();
    private static final String convertPythonScript = "pickle2python.py";
    // TODO: 2016/5/26 notice: begin from index 1 because the demo runs on Windows machine, not tested yet on Linux
    private static final String scriptPath = StrategyImpl.class.getResource(convertPythonScript).getPath().substring(1);

    private static String ld2str(LocalDate date) {
        return String.format("%d-%d-%d", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    @RequestMapping(method = RequestMethod.POST, value = "run")
    public String runStrategy(@RequestBody String code) throws IOException {
        System.out.println(code);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL("http://104.236.174.190:5000");
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type",
                    "application/json");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(code);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
            return result;
        }

    }

    public JSONObject runStrategy(@ModelAttribute("code") String pythonCode, LocalDate start, LocalDate end, double capital) {
        System.out.println("============python code=========");
        System.out.println(pythonCode);
        System.out.println("============python code=========");

        String fileName = "tmp" + LocalDate.now().hashCode();
        String fullPathName = tmpPath + "/" + fileName;
        try {
            FileWriter fw = new FileWriter(fullPathName + ".py");
            fw.write(pythonCode);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String command = String.format("zipline run -f %s -s %s -e %s -o %s --capital-base %f",
                    fullPathName + ".py",
                    ld2str(start),
                    ld2str(end),
                    fullPathName + ".pickle",
                    capital);
            System.out.println("run command: " + command);
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

            System.out.println("================result==============");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();

            System.out.println("================to json==============");
            String runToJsonCommand = String.format("python %s %s %s",
                    scriptPath, tmpPath, fileName);
            System.out.println("run command: " + runToJsonCommand);
            Process toJsonProcess = Runtime.getRuntime().exec(runToJsonCommand);

            process.waitFor();

            System.out.println("================result==============");
            br = new BufferedReader(new InputStreamReader(toJsonProcess.getInputStream()));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();

            System.out.println("===============read from json file==============");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fullPathName + ".json")));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return JSONObject.fromObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
