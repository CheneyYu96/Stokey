package org.xeon.stockey.data.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.po.StockInfoPO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NetworkHelper {
	public Iterator<DailyDataPO> getDailyData(String symbol, Calendar startDate, Calendar endDate){
		String start = UtilityTools.Cal2String(startDate);
		String end = UtilityTools.Cal2String(endDate);
		JSONArray ja = this.getJSONArray(symbol, start, end);
		if(ja==null) return null;
		ArrayList<DailyDataPO> array = new ArrayList<DailyDataPO>();
		for(int i=0;i<ja.size();i++){
			JSONObject jo = ja.getJSONObject(i);
			DailyDataPO dailyData = this.JSON2DailyData(jo, symbol);
			array.add(dailyData);
		}
		return array.iterator();
	}
	public Iterator<DailyDataPO> getDailyData(String symbol){
		JSONArray ja = this.getJSONArray(symbol, null, null);
		if(ja==null) return null;
		ArrayList<DailyDataPO> array = new ArrayList<DailyDataPO>();
		for(int i=0;i<ja.size();i++){
			JSONObject jo = ja.getJSONObject(i);
			DailyDataPO dailyData = this.JSON2DailyData(jo, symbol);
			array.add(dailyData);
		}
		return array.iterator();
	}
	JSONArray getJSONArray(String symbol, String startDate, String endDate){
		BufferedReader reader = null;
		String result = "";
		StringBuffer sbf = new StringBuffer();
		String httpUrl = "http://121.41.106.89:8010/api/stock/"+symbol+"/";
		if(startDate!=null&&endDate!=null)
			httpUrl += "?start="+startDate+"&end="+endDate;
		if(symbol.equals("bm000300")) httpUrl = "http://121.41.106.89:8010/api/benchmark/hs300";
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
			JSONArray ja = JSONObject.fromObject(result).getJSONObject("data")
					.getJSONArray("trading_info");
			return ja;
		} catch (Exception e) {
			System.err.println("get network info error!");
			return null;
		}
	}
	DailyDataPO JSON2DailyData(JSONObject jo,String symbol){
		DailyDataPO dailyData = new DailyDataPO();
		dailyData.setAdjPrice(jo.getDouble("adj_price"));
		dailyData.setClose(jo.getDouble("close"));
		dailyData.setHigh(jo.getDouble("high"));
		dailyData.setLow(jo.getDouble("low"));
		dailyData.setOpen(jo.getDouble("open"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance(); 
		try {
			cal.setTime(sdf.parse(jo.getString("date")));
		} catch (ParseException e) {
			System.err.println("parse failed!!!");
		}
		dailyData.setId(symbol.substring(2)+ jo.getString("date").replace("-", ""));
		dailyData.setTheDate(cal);
		if(!symbol.equals("bm000300")){
			dailyData.setPb(jo.getDouble("pb"));
			dailyData.setPe(jo.getDouble("pe_ttm"));
			dailyData.setTurnover(jo.getDouble("turnover"));
		}

		dailyData.setVolumn(jo.getDouble("volume"));
		return dailyData;
	}

	public StockInfoPO getStockInfo(String symbol){
		StockInfoPO stock = new StockInfoPO();
		if(symbol.substring(0, 2).equals("sh"))
			stock.setMarket(ExchangeEnum.SHANGHAI);
		else
			stock.setMarket(ExchangeEnum.SHENZHEN);
		BufferedReader reader = null;
		String httpUrl = "http://quote.eastmoney.com/"+symbol+".html";
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "GBK"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				if(strRead.contains("行业</td>")){
					stock.setType(strRead.split(">")[1].split("<")[0]);
				}
				if(strRead.contains("<title>")){
					stock.setName(strRead.split(">")[1].split("\\(")[0]);
					stock.setSymbol(symbol);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		httpUrl = "http://stockpage.10jqka.com.cn/"+symbol.substring(2)+"/";
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// 填入apikey到HTTP header
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				if(strRead.contains("所属地域"))
					stock.setRegion(reader.readLine().split(">")[1].split("<")[0]);
				if(strRead.contains("上市日期"))
					stock.setInfo("上市日期："+reader.readLine().split(">")[1].split("<")[0]);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(stock.getType()==null) stock.setType("不详");
		return stock;
	}
	public static void main(String[] args){
		NetworkHelper helper = new NetworkHelper();
		JSONArray ja = helper.getJSONArray("sh600036", "2016-04-01", "2016-04-10");
		int i;
		for(i=0;i<ja.size();i++){
			DailyDataPO po = helper.JSON2DailyData(ja.getJSONObject(i),"sh600000");
			po.print();
		}
//		System.out.println(i);
//		StockInfoPO po = helper.getStockInfo("sh600036");
//		po.print();
	}

}
