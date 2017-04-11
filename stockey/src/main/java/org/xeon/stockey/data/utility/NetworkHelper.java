package org.xeon.stockey.data.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.po.StockInfoPO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NetworkHelper {
    public Iterator<DailyDataPO> getDailyData(String symbol, Calendar startDate, Calendar endDate) {
        String start = UtilityTools.Cal2String(startDate);
        String end = UtilityTools.Cal2String(endDate);
        JSONArray ja = this.getJSONArray(symbol, start, end);
        if (ja == null) return null;
        ArrayList<DailyDataPO> array = new ArrayList<DailyDataPO>();
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            DailyDataPO dailyData = this.JSON2DailyData(jo, symbol);
            array.add(dailyData);
        }
        return array.iterator();
    }

    public Iterator<DailyDataPO> getDailyData(String symbol) {
        JSONArray ja = this.getJSONArray(symbol, null, null);
        if (ja == null) return null;
        ArrayList<DailyDataPO> array = new ArrayList<DailyDataPO>();
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            DailyDataPO dailyData = this.JSON2DailyData(jo, symbol);
            array.add(dailyData);
        }
        return array.iterator();
    }

    JSONArray getJSONArray(String symbol, String startDate, String endDate) {
        BufferedReader reader = null;
        String result = "";
        StringBuffer sbf = new StringBuffer();
        String httpUrl = "http://121.41.106.89:8010/api/stock/" + symbol + "/";
        if (startDate != null && endDate != null)
            httpUrl += "?start=" + startDate + "&end=" + endDate;
        if (symbol.equals("bm000300")) {
            httpUrl = "http://121.41.106.89:8010/api/benchmark/hs300";
            httpUrl += "?start=" + startDate + "&end=" + endDate;
        }
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("X-Auth-Code", "d0e88be6addea5f25186aa3ad46b2bcc");
            // 填入apikey到HTTP header
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "GBK"));
            //reader = new BufferedReader(new FileReader(new File("test.json")));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                result += strRead;
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

    DailyDataPO JSON2DailyData(JSONObject jo, String symbol) {
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
        dailyData.setId(symbol.substring(2) + jo.getString("date").replace("-", ""));
        dailyData.setTheDate(cal);
        if (!symbol.equals("bm000300")) {
            dailyData.setPb(jo.getDouble("pb"));
            dailyData.setPe(jo.getDouble("pe_ttm"));
            dailyData.setTurnover(jo.getDouble("turnover"));
        }

        dailyData.setVolumn(jo.getDouble("volume"));
        return dailyData;
    }

    public Iterator<String> getAllStockCode() {
        BufferedReader reader = null;
        String result = "";
        StringBuffer sbf = new StringBuffer();
        String[] exchanges = {"sz"};
        ArrayList<String> stockCodes = new ArrayList<>();
        for (String exchange : exchanges) {
            String httpUrl = "http://121.41.106.89:8010/api/stocks?year=2016&exchange=" + exchange;
            try {
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.addRequestProperty("X-Auth-Code", "d0e88be6addea5f25186aa3ad46b2bcc");
                // 填入apikey到HTTP header
                connection.connect();
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, "GBK"));
                //reader = new BufferedReader(new FileReader(new File("test.json")));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    result += strRead;
                }
                reader.close();
                JSONArray ja = JSONObject.fromObject(result).getJSONArray("data");
                for (int i = 0; i < ja.size(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    stockCodes.add(jo.getString("name"));

                }
            } catch (Exception e) {
                System.err.println("get network info error!");
                return null;
            }
        }
        return stockCodes.iterator();
    }

    public String getHtmlA(String symbol) {
        BufferedReader reader = null;
        String result = "";
        String httpUrl = "http://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpInfo/stockid/" + symbol + ".phtml";
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "GBK"));
            //reader = new BufferedReader(new FileReader(new File("test.json")));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                //System.out.println(strRead);
                result += strRead + "\n";
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("get network info error!");
        }
        return result;
    }

    public String getHtmlB(String symbol) {
        BufferedReader reader = null;
        String result = "";
        String httpUrl = "http://vip.stock.finance.sina.com.cn/corp/go.php/vCI_CorpOtherInfo/stockid/" + symbol + "/menu_num/5.phtml";
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "GBK"));
            //reader = new BufferedReader(new FileReader(new File("test.json")));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                //System.out.println(strRead);
                result += strRead + "\n";
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("get network info error!");
        }
        return result;
    }

    public StockInfoPO getStockInfo(String symbol) {
        StockInfoPO po = new StockInfoPO();
        po.setSymbol(symbol);
        String result = this.getHtmlA(symbol);
//        System.out.println(result);
        String[] lines = result.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String s = lines[i];
            if(po.getName()==null) {
                if (s.contains("<a href=\"http://finance.sina.com.cn/realstock/company/sh" + symbol + "/nc.shtml\">") ||
                        s.contains("<a href=\"http://finance.sina.com.cn/realstock/company/sz" + symbol + "/nc.shtml\">")) {
                    if (s.contains("公司简介")) {
                        String tmp = this.deHtml(s);
                        //System.out.println(tmp);
                        po.setName(tmp.split(" &gt; ")[2]);
                    }
                }
            }
            if (s.contains("上市市场：")) {
                String info = lines[i + 1];
                po.setMarket(this.deHtml(info));
            }
            if (s.contains("公司简介：")) {
                String info = lines[i + 1];
                po.setInfo(this.deHtml(info));
            }
            if (s.contains("注册资本：")) {
                String info = lines[i + 1];
                po.setRegisterCapital(this.deHtml(info));
            }
            if (s.contains("组织形式：")) {
                String info = lines[i + 1];
                po.setOrganize(this.deHtml(info));
            }
            if (s.contains("公司英文名称：")) {
                String info = lines[i + 1];
                po.setEnglishName(this.deHtml(info));
            }
            if (s.contains("注册地址：")) {
                String info = lines[i + 1];
                po.setRegion(this.deHtml(info).substring(0, 3));
            }

        }


        result = this.getHtmlB(symbol);
        lines = result.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String s = lines[i];
            if (s.contains("同行业个股")) {
                String info = lines[i + 4];
                po.setType(this.deHtml(info));
            }
        }
        return po;
    }

    public String deHtml(String info) {
        Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(info);
        info = matcher.replaceAll("").trim();
        return info;
    }

    public static void main(String[] args) {
        NetworkHelper helper = new NetworkHelper();
        StockInfoPO po = helper.getStockInfo("300411");
//        Iterator<String> it = helper.getAllStockCode();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//        }
//		JSONArray ja = helper.getJSONArray("sh600036", "2016-04-01", "2016-04-10");
//		int i;
//		for(i=0;i<ja.size();i++){
//			DailyDataPO po = helper.JSON2DailyData(ja.getJSONObject(i),"sh600000");
//			po.print();
//		}
//		System.out.println(i);
//		StockInfoPO po = helper.getStockInfo("sh600036");
//		po.print();
    }

}
