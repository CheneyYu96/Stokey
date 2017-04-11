package org.xeon.stockey.businessLogic.strategy;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.xeon.stockey.businessLogicService.strategyService.StrategyService;
import org.xeon.stockey.ui.utility.DateUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;

/**
 * Created by yuminchen on 16/5/31.
 * get strategy from Internet
 */
public class StrategyServiceStub implements StrategyService{
    private final String TARGET_URL = "http://104.236.174.190:5000";

    public JSONObject runStrategy( String pythonCode, LocalDate start, LocalDate end, double capital) {

        System.out.println("========================");
        System.out.println(pythonCode);
        System.out.println("========================");

        BufferedReader reader ;

        String result = "";

        JSONObject returnJson = null;
        try {
            URL url = new URL(TARGET_URL);
            HttpURLConnection conn = null;
            URLConnection connection = url.openConnection();
            if(connection instanceof HttpURLConnection){
                conn = (HttpURLConnection) connection;
            }
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            System.err.println(start.toString());
            /* send request to the service*/
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            JSONObject object = new JSONObject();
            object.element("code",pythonCode);
            object.element("begin_date", DateUtil.toString(start));
            object.element("end_date",DateUtil.toString(end));
            object.element("capital",capital);

            out.writeBytes(object.toString());
            out.flush();
            out.close();

            /* read the response*/
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer buffer = new StringBuffer("");
            while ((result = reader.readLine())!=null){
                buffer.append(result);
            }

            reader.close();
            conn.disconnect();
            returnJson = JSONObject.fromObject(buffer.toString());
            System.out.println(returnJson);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnJson;


    }

}
