package org.xeon.stockey.data.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.po.StockInfoPO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 从网上把StockInfo爬下来
 * 
 * @author nians
 *
 */
//d0e88be6addea5f25186aa3ad46b2bcc
public class StockDataInitializer implements Runnable{
	private static Queue<String> symbolQ = new PriorityQueue<String>();
	private static ArrayList<StockInfoPO> result = new ArrayList<StockInfoPO>();
	private static StockDataService data = DataServiceFactory.getStockDataService();
	private static int num = 0;
	NetworkHelper netHelper;
	public StockDataInitializer(){
		this.netHelper = new NetworkHelper();
	}

	private Iterator<String> getStockList(String exchange){
		BufferedReader reader = null;
		String result = "";
		StringBuffer sbf = new StringBuffer();
		String httpUrl = "http://121.41.106.89:8010/api/stocks/?year=2016&exchange="+exchange;
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<String> stocks = new ArrayList<String>();
		JSONArray ja = JSONObject.fromObject(result).getJSONArray("data");
		for(int i=0;i<ja.size();i++){
			stocks.add(ja.getJSONObject(i).getString("name"));
		}
		ArrayList<String> temp = new ArrayList<String>();
		for(int i=0;i<5;i++){
			temp.add(stocks.get((int)(Math.random()*1000)));
		}
		return temp.iterator();
	}
	public StockInfoPO addStock(String symbol) {
		StockInfoPO stock = new StockInfoPO();
		if(symbol.substring(0, 2).equals("sh")){
			if(symbol.equals("sh000300")){
				stock.setMarket("BENCHMARK");
				stock.setInfo("沪深两市300数据");
				stock.setName("沪深300");
				stock.setRegion("不可用");
				stock.setSymbol("bm000300");
				stock.setType("不可用");
				Iterator<DailyDataPO> it = this.addDailyData(symbol);
//				while(it.hasNext()) stock.addDailyData(it.next());
//				System.out.println(symbol);
				return stock;
			}
			else	stock.setMarket("SHANGHAI");
		}
		else
			stock.setMarket("SHENZHEN");
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
//		Iterator<DailyDataPO> it = this.addDailyData(symbol);
//		while(it.hasNext()) stock.addDailyData(it.next());
		System.out.println(symbol);
		return stock;
	}
	public Iterator<DailyDataPO> addDailyData(String symbol) {
		JSONArray ja = netHelper.getJSONArray(symbol,null,null);
		ArrayList<DailyDataPO> array = new ArrayList<DailyDataPO>();
		for(int i=0;i<ja.size();i++){
			JSONObject jo = ja.getJSONObject(i);
			DailyDataPO dailyData = netHelper.JSON2DailyData(jo, symbol);
			array.add(dailyData);
		}
		return array.iterator();

	}
	public void initialize(){

	}
	public static void main(String[] args){
		StockDataInitializer init = new StockDataInitializer();
		Iterator<String> it = init.getStockList("sh");
		while(it.hasNext()){
			String symbol = it.next();
			symbolQ.add(symbol);
		}
		Iterator<String> it2 = init.getStockList("sz");
		while(it2.hasNext()){
			String symbol = it2.next();
			symbolQ.add(symbol);
		}
		Thread th1 = new Thread(new StockDataInitializer());
		Thread th2 = new Thread(new StockDataInitializer());
		Thread th3 = new Thread(new StockDataInitializer());
		Thread th4 = new Thread(new StockDataInitializer());
		Thread th5 = new Thread(new StockDataInitializer());
		Thread th6 = new Thread(new StockDataInitializer());
		Thread th7 = new Thread(new StockDataInitializer());
		Thread th8 = new Thread(new StockDataInitializer());
		Thread th9 = new Thread(new StockDataInitializer());
		Thread th10 = new Thread(new StockDataInitializer());
		Thread th11 = new Thread(new StockDataInitializer());
		Thread th12 = new Thread(new StockDataInitializer());
		Thread th13 = new Thread(new StockDataInitializer());
		Thread th14 = new Thread(new StockDataInitializer());
		Thread th15 = new Thread(new StockDataInitializer());
		Thread th16 = new Thread(new StockDataInitializer());
		Thread th17 = new Thread(new StockDataInitializer());
		Thread th18 = new Thread(new StockDataInitializer());
		Thread th19 = new Thread(new StockDataInitializer());
		Thread th20 = new Thread(new StockDataInitializer());
		th1.start();
		th2.start();
		th3.start();
		th4.start();
		th5.start();
		th6.start();
		th7.start();
		th8.start();
		th9.start();
		th10.start();
		th11.start();
		th12.start();
		th13.start();
		th14.start();
		th15.start();
		th16.start();
		th17.start();
		th18.start();
		th19.start();
		th20.start();
		

	}

	@Override
	public void run() {
		String symbol = null;
		while(!symbolQ.isEmpty()){
			synchronized(this){
				if(!symbolQ.isEmpty()){
					symbol = symbolQ.poll();
				}
			}
			if(symbol==null) break;
			StockInfoPO stock = this.addStock(symbol);
			result.add(stock);
			symbol = null;
		}
		num++;
		save();
	}
	
	private static void save(){
//		if(num<19) return; 
//		data.addStockBat(result.iterator());
	}

}
