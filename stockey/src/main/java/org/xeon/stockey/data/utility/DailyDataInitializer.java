package org.xeon.stockey.data.utility;

/**
 * 从网上把StockInfo爬下来
 * 
 * @author nians
 *
 */
//d0e88be6addea5f25186aa3ad46b2bcc
public class DailyDataInitializer {
//	NetworkHelper netHelper;
//	
//	public DailyDataInitializer(){
//		this.netHelper = new NetworkHelper();
//	}
//	
//	private Iterator<String> getStockList(String exchange){
//		BufferedReader reader = null;
//		String result = "";
//		StringBuffer sbf = new StringBuffer();
//		String httpUrl = "http://121.41.106.89:8010/api/stocks/?year=2016&exchange="+exchange;
//		try {
//			URL url = new URL(httpUrl);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("GET");
//			connection.addRequestProperty("X-Auth-Code", "d0e88be6addea5f25186aa3ad46b2bcc");
//			// 填入apikey到HTTP header
//			connection.connect();
//			InputStream is = connection.getInputStream();
//			reader = new BufferedReader(new InputStreamReader(is, "GBK"));
//			String strRead = null;
//			while ((strRead = reader.readLine()) != null) {
//				result+=strRead;
//			}
//			reader.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		ArrayList<String> stocks = new ArrayList<String>();
//		JSONArray ja = JSONObject.fromObject(result).getJSONArray("data");
//		for(int i=0;i<ja.size();i++){
//			stocks.add(ja.getJSONObject(i).getString("name"));
//		}
//		return stocks.iterator();
//	}
//	
//	
//
//	public static void main(String[] args){
//		DailyDataInitializer init = new DailyDataInitializer();
//		ArrayList<DailyDataPO> result = new ArrayList<DailyDataPO>();
//		Iterator<String> it = init.getStockList("sh");
//		int num = 100;
//		while(it.hasNext()){
//			String symbol = it.next();
//			if(symbol.equals("sh000300"))
//				continue;
//			System.out.println(symbol);
//			Iterator<DailyDataPO> a = init.addDailyData(symbol);
//			while(a.hasNext())
//				result.add(a.next());
//		}
//		Iterator<String> it2 = init.getStockList("sz");
//		while(it2.hasNext()){
//			String symbol = it2.next();
//			System.out.println(symbol);
//			Iterator<DailyDataPO> a = init.addDailyData(symbol);
//			while(a.hasNext())
//				result.add(a.next());
//		}
//		StockData data = new StockData();
//		for(DailyDataPO po : result) System.out.println(po.getId());
//		data.addDailyDataBat(result.iterator());
//	}
}
