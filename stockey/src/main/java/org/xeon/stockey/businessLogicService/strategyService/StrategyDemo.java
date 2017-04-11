package org.xeon.stockey.businessLogicService.strategyService;

import net.sf.json.JSONObject;
import org.xeon.stockey.util.R;

import java.io.*;
import java.time.LocalDate;

/**
 * a demo to run strategy from java
 * Created by Sissel on 2016/5/13.
 */
public class StrategyDemo
{
    private static final String tmpPath = "E://tmp";
    private static final String convertPythonScript = "pickle2python.py";
    // TODO: 2016/5/26 notice: begin from index 1 because the demo runs on Windows machine, not tested yet on Linux
    private static final String scriptPath = StrategyDemo.class.getResource(convertPythonScript).getPath().substring(1);

    private static String ld2str(LocalDate date)
    {
        return String.format("%d-%d-%d", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    private static void handleStrategy(String pythonCode, LocalDate start, LocalDate end, double capital)
    {
        System.out.println("============python code=========");
        System.out.println(pythonCode);
        System.out.println("============python code=========");

        String fileName = "tmp" + LocalDate.now().hashCode();
        String fullPathName = tmpPath + "/" + fileName;
        try
        {
            FileWriter fw = new FileWriter(fullPathName + ".py");
            fw.write(pythonCode);
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
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
            while ((line = br.readLine()) != null)
            {
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
            while ((line = br.readLine()) != null)
            {
                System.out.println(line);
            }
            br.close();

            System.out.println("===============read from json file==============");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fullPathName + ".json")));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            JSONObject jsonObject = JSONObject.fromObject(sb.toString());
            System.out.println(jsonObject.getJSONObject("2013-01-02T21:00:00.000Z"));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("E://tmp/hello.py"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
                sb.append("\n");
            }
            handleStrategy(sb.toString(), LocalDate.of(2013, 1, 1), LocalDate.of(2014, 1, 1), 10000000);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
