package org.xeon.stockey.data.utility;

import org.apache.commons.beanutils.converters.DateConverter;
import org.xeon.stockey.businessLogic.stockAnalysis.StockAnalysisController;
import org.xeon.stockey.businessLogic.utility.DateTypeConverter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.data.dao.DailyDataDAO;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.ui.utility.ExceptionTips;
import org.xeon.stockey.vo.KdjVO;
import org.xeon.stockey.vo.MacdVO;
import org.xeon.stockey.vo.RsiVO;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by nians on 2016/5/31.
 */
public class TrainDataGenerator {

    //默认预测股票为sh600036招商银行
    private final String[] codes = {"sh600036","sh600048","sh600606","sh601288","sh601398"
            ,"sz000031","sz000402"};

    private final String[] bus = {"sh601288","sh601398","sz002142"};
    private final String[] region = {"sh600048","sz000031"};
    private final String[] market = {"sh600048","sh600606","sh601288","sh601398"};

    DailyDataDAO dao;
    StockAnalysisController controller;

    public TrainDataGenerator() {
        MySessionFactory sessionFactory = new MySessionFactory();
        dao = new DailyDataDAO();
        controller = new StockAnalysisController();
    }

    /**
     * 获得一个训练实例
     * @return 一个csv字符串
     * kValue,dValue,jValue,
     * dif,dem,osc,
     * rsi6,rsi12,rsi24,
     * classValue
     */
    private String getInstance() throws NetworkConnectionException {
        String result = "";
        String code = codes[(int) (Math.random() * codes.length)];
        Calendar date = this.getRandomMonday();
        LocalDate localDate = DateTypeConverter.convert(date);
        Collection<KdjVO> kdjs = controller.getKdjD(code, localDate, localDate);
        KdjVO kdj = kdjs.iterator().next();
        Collection<MacdVO> macds = controller.getMacdD(code, localDate, localDate);
        MacdVO macd = macds.iterator().next();
        Collection<RsiVO> rsis = controller.getRsiD(code, localDate, localDate);
        RsiVO rsi = rsis.iterator().next();

        result += kdj.kValue + ",";
        result += kdj.dValue + ",";
        result += kdj.jValue + ",";

        result += macd.dem + ",";
        result += macd.dif + ",";
        result += macd.osc + ",";

        result += rsi.rsi6 + ",";
        result += rsi.rsi12 + ",";
        result += rsi.rsi24 + ",";


        result += this.getY(code,date) + "\n";
        return result;
    }

    private String getY(String code, Calendar date){
        date.add(Calendar.DATE,1);
        String id = code.substring(2)+UtilityTools.Cal2String(date).replace("-","");
        DailyDataPO po = dao.findDailyData(id);
        return (po.getClose()-po.getOpen()>0)?"up":"down";
    }

    private String regularize(double src) {
        double tmp = src;
        if(src>100) {
            int digit = (int) Math.log10(src);
            // System.out.println("digit is " + digit);
            tmp = src / Math.pow(10, digit - 1);
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(tmp);
    }

    private Calendar getRandomMonday(){
        String[] seeds = {"2016-05-30", "2016-05-31", "2016-06-01", "2016-06-02"};
        return this.getRandomDay(seeds[(int) (seeds.length * Math.random())]);
    }

    private Calendar getRandomDay(String seed) {
        Calendar cal = UtilityTools.String2Cal(seed);
        int bias = -1 * (int) ((Math.random() * 230 + 1));
        cal.add(Calendar.WEEK_OF_YEAR,bias);
        return cal;
    }

    public static void main(String[] args) throws NetworkConnectionException, IOException {
        //System.out.println(TrainDataGenerator.regularize(1.866422E7));
        TrainDataGenerator trainDataGenerator = new TrainDataGenerator();
//        trainDataGenerator.trry();
        ArrayList<String> result = new ArrayList<>();

        FileWriter fileWriter = new FileWriter(new File("./test9.csv"));

        for (int i = 0; i < 300; i++) {
            System.out.println("*************" + i + "************");
            try {
                String s = trainDataGenerator.getInstance();
                if (!result.contains(s)) {
                    result.add(s);
                    fileWriter.write(s);
                    fileWriter.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }


        for(String s : result){
            System.out.println(s);
        }

    }

    public static void main1(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(new File("./test.csv")));
        ArrayList<String> src = new ArrayList<>();
        String tmp = bf.readLine();
        while (tmp!=null) {
            src.add(tmp);
            tmp = bf.readLine();
        }



    }
}
