package org.xeon.stockey.data.utility;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.data.dao.DailyDataDAO;
import org.xeon.stockey.data.dao.StockInfoDAO;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.data.impl.StockData;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StockInfoPO;
import org.xeon.stockey.po.StrategyPO;

import static jdk.nashorn.internal.objects.Global.load;

public class SQLInitializer implements  Runnable{
    FileWriter fileWriter;
    static ArrayList<String> stockList;
    static NetworkHelper helper;
    static MySessionFactory factory;
    StockInfoDAO dao;

    public SQLInitializer() {
        String fileName = "log.txt";
        try {
            fileWriter = new FileWriter(fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.dao = new StockInfoDAO();
    }

    public static void in() throws IOException {
        stockList = new ArrayList<>();
        helper = new NetworkHelper();
        BufferedReader reader = new BufferedReader(new FileReader(new File("./AllStockList.txt")));
        String tmp = reader.readLine();
        while (tmp != null) {
            stockList.add(tmp);
            tmp = reader.readLine();
        }

        //factory = new MySessionFactory();
    }

    public static void main(String[] args) throws IOException {
        try {
            SQLInitializer.in();

        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLInitializer initializer = new SQLInitializer();
        initializer.initializeBenchmark();
//        Thread th1 = new Thread(new SQLInitializer());
//        Thread th2 = new Thread(new SQLInitializer());
//        Thread th3 = new Thread(new SQLInitializer());
//        Thread th4 = new Thread(new SQLInitializer());
//        Thread th5 = new Thread(new SQLInitializer());
//
//        th1.start();
//        th2.start();
//        th3.start();
//        th4.start();
//        th5.start();

//        SQLInitializer sqlInitializer = new SQLInitializer();
//        NetworkHelper helper = new NetworkHelper();
//        Iterator<String> it = helper.getAllStockCode();
//
//        try {
//            sqlInitializer.initialize();
//            //sqlInitializer.initializeBenchmark();
////            while (it.hasNext()) {
////                sqlInitializer.writeLog(it.next()+"\n");
////        }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void initialize() throws IOException {
        MySessionFactory f = new MySessionFactory();
        NetworkHelper helper = new NetworkHelper();
        DailyDataDAO dailyDataDAO = new DailyDataDAO();

        ArrayList<String> getAllStockList = this.getList("AllStockList.txt");
        ArrayList<String> completeList = this.getList("log.txt");
        Iterator<String> codeIt = getAllStockList.iterator();
        int i = 0;
        while (codeIt.hasNext()) {
            String code = codeIt.next();
            if (completeList.contains(code)) {
                System.err.println("jump over");
                continue;
            }
            Calendar startDate = UtilityTools.String2Cal("2016-06-11");
            Calendar endDate = Calendar.getInstance();
            if (startDate.equals(endDate)) continue;
            startDate.add(Calendar.DATE, 1);


            Iterator<DailyDataPO> poIterator = helper.getDailyData(code, startDate, endDate);
            dailyDataDAO.addDailyDataBat(poIterator);
            i++;
            this.writeLog("Done with : " + code + i / 2587.0 + "%\n");
        }
    }

    public void initializeBenchmark() throws IOException{
        MySessionFactory f = new MySessionFactory();
        NetworkHelper helper = new NetworkHelper();
        DailyDataDAO dailyDataDAO = new DailyDataDAO();

        Calendar startDate = UtilityTools.String2Cal("2016-06-10");
        Calendar endDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 1);


        Iterator<DailyDataPO> poIterator = helper.getDailyData("bm000300", startDate, endDate);
        dailyDataDAO.addDailyDataBat(poIterator);
    }

    public void run(){
            while(!stockList.isEmpty()){
                String s = "";
                synchronized(this){
                    s = stockList.get(0);
                    stockList.remove(0);
                }
                StockInfoPO po = helper.getStockInfo(s.substring(2));
                System.out.println("update stockinfopo set name = \""+po.getName()+"\" where symbol = \""+s+"\";");
            }


    }

    private ArrayList<String> getList(String fileName) {
        boolean flag = false;
        if (fileName.equals("log.txt")) flag = true;
        ArrayList<String> result = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            String tmp = br.readLine();
            while (tmp != null) {
                if (flag) {
                    tmp = tmp.split(" : ")[1].split("\\.")[0].substring(0, 8);
                }
                result.add(tmp);
                tmp = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void loadDailyData(Iterator<DailyDataPO> it, ArrayList<DailyDataPO> src) {
        while (it.hasNext()) {
            src.add(it.next());
        }
    }

    public static void loadStockInfo(Iterator<StockInfoPO> it, ArrayList<StockInfoPO> src) {
        while (it.hasNext()) {
            src.add(it.next());
        }
    }

    private void writeLog(String log) throws IOException {
        fileWriter.write(log);
        fileWriter.flush();
    }

}
