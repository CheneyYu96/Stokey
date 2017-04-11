package org.xeon.stockey.businessLogic.stockAnalysis;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xeon.stockey.vo.NewsVO;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nians on 2016/6/18.
 */
@RestController
@RequestMapping("/stock/news")
public class StockNews {

    @RequestMapping("/get")
    public Collection<NewsVO> getStockNews(@RequestParam String stockCode) {
        ArrayList<NewsVO> result = new ArrayList<>();

        ArrayList<String> lines = this.getHtmlLines(stockCode);
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            String s3 = "";
            if (i + 3 < lines.size())
                s3 = lines.get(i + 3);
            else break;
            if (s.contains("</li>\t\t\t\t<li>") && s3.contains("href=\"")) {
                NewsVO vo = new NewsVO();
                vo.setTime(this.deHtml(lines.get(i + 1)));
                vo.setLink(s3.split("href=\"")[1].split("\"")[0]);
                vo.setBrief(this.deHtml(lines.get(i + 3)));
                result.add(vo);
            }
        }

        return result;
    }

    public ArrayList<String> getHtmlLines(String symbol) {
        BufferedReader reader = null;
        ArrayList<String> result = new ArrayList<>();
        String httpUrl = "http://finance.sina.com.cn/realstock/company/" + symbol + "/nc.shtml";
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
                result.add(strRead);
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("get network info error!");
        }
        return result;
    }

    public String deHtml(String info) {
        Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(info);
        info = matcher.replaceAll("").trim();
        return info;
    }

    public static void main(String[] args) {
        StockNews stockNews = new StockNews();
        Collection<NewsVO> tmp = stockNews.getStockNews("sh600036");
        for (NewsVO s : tmp) {
            System.out.println(s);
        }
    }

}
