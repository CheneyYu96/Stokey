package simpleTests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestForAnyquant {
	public static void main(String[] args){
		BufferedReader reader = null;
		String result = "";
		StringBuffer sbf = new StringBuffer();
		String httpUrl = "http://121.41.106.89:8010/api/benchmark/hs300?start=2016-03-01&end=2016-04-12";
		try{
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.addRequestProperty("X-Auth-Code", "d0e88be6addea5f25186aa3ad46b2bcc");
			// 填入apikey到HTTP header
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "GBK"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				result+=strRead;
			}
			reader.close();
			System.out.println(result);
		} catch (Exception e) {
			System.err.println("get network info error!");
		}
	}
}
