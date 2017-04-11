package org.xeon.stockey.data.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.xeon.stockey.businessLogic.utility.*;
import org.xeon.stockey.data.dao.DailyDataDAO;
import org.xeon.stockey.data.dao.StockInfoDAO;
import org.xeon.stockey.data.utility.NetworkHelper;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.po.StockInfoPO;

public class StockData implements StockDataService {
    StockInfoDAO stockDao;
    DailyDataDAO dailyDao;
    NetworkHelper netHelper;

    StockData() {
        this.stockDao = new StockInfoDAO();
        this.dailyDao = new DailyDataDAO();
        this.netHelper = new NetworkHelper();
        DataWarehouse.init(dailyDao);
    }

    @Override
    public Iterator<DailyDataPO> getStockDaily(String symbol, Calendar startDate, Calendar endDate) {
        if (symbol.length() != 6) {
            symbol = symbol.substring(2);
        }
        if (startDate == null || endDate == null) {
            endDate = Calendar.getInstance();
            startDate = Calendar.getInstance();
            startDate.add(Calendar.DATE, -31);
        }
        Iterator<DailyDataPO> it = DataWarehouse.attempt(symbol, startDate, endDate);
        if (it != null) return it;
        else
            return dailyDao.findDailyData(symbol, startDate, endDate);
    }

    @Override
    public DailyDataPO getLatestDailyData(String stockCode) {
        Calendar calendar = dailyDao.getLatestForStock(stockCode);
        String tmp = stockCode;
        if (tmp.length() != 6) tmp = tmp.substring(2);
        tmp = tmp + UtilityTools.Cal2String(calendar).replace("-", "");
        return dailyDao.findDailyData(tmp);
    }

    @Override
    public StockInfoPO getStockInfo(String stockCode) {
        return stockDao.findStockInfo(stockCode);
    }

    @Override
    public Iterator<StockInfoPO> getAllStock() throws NetworkConnectionException {
        return stockDao.getAllStock("");
    }

    @Override
    public Iterator<DailyDataPO> searchStock(Iterator<Filter> criteria) {
        ArrayList<Filter> arrayList = new ArrayList<>();
        while (criteria.hasNext()) {
            arrayList.add(criteria.next());
        }
        String id = null;
        Calendar startDate = null;
        Calendar endDate = null;
        for (int i = 0; i < arrayList.size(); i++) {
            boolean change = false;
            Filter filter = arrayList.get(i);
            if (filter.fieldType == Filter.FieldType.id && filter.compareType == Filter.CompareType.START_WITH) {
                id = (String) filter.value;
                change = true;
            }
            if (filter.fieldType == Filter.FieldType.theDate && filter.compareType == Filter.CompareType.BET) {
                startDate = (Calendar) filter.value;
                change = true;
            }
            if (filter.fieldType == Filter.FieldType.theDate && filter.compareType == Filter.CompareType.LET) {
                endDate = (Calendar) filter.value;
                change = true;
            }
            if (!change) return this.dailyDao.conditionSearch(criteria);
        }
        if (id == null || startDate == null || endDate == null)
            return this.dailyDao.conditionSearch(criteria);
        else
            return this.getStockDaily(id, startDate, endDate);
    }

    @Override
    public Iterator<StockInfoPO> searchStockInfo(Iterator<Filter> criteria) {
        ArrayList<Filter> arrayList = new ArrayList<>();
        while (criteria.hasNext()) {
            arrayList.add(criteria.next());
        }
        String id = null;
        for (int i = 0; i < arrayList.size(); i++) {
            boolean change = false;
            Filter filter = arrayList.get(i);
            if (filter.fieldType == Filter.FieldType.symbol && filter.compareType == Filter.CompareType.EQ) {
                id = (String) filter.value;
                change = true;
            }
            if (!change) this.stockDao.conditionSearch(arrayList.iterator());
        }
        if (id != null && arrayList.size() == 1) {
            ArrayList<StockInfoPO> list = new ArrayList<>();
            list.add(this.stockDao.findStockInfo(id));
            return list.iterator();
        } else
            return this.stockDao.conditionSearch(arrayList.iterator());
    }

    @Override
    public Iterator<StockInfoPO> getBenchmark() throws NetworkConnectionException {
        ArrayList<StockInfoPO> array = new ArrayList<StockInfoPO>();
        StockInfoPO stock = new StockInfoPO();
        stock.setMarket("BENCHMARK");
        stock.setInfo("沪深两市300数据");
        stock.setName("沪深300");
        stock.setRegion("不可用");
        stock.setSymbol("bm000300");
        stock.setType("不可用");
//		Iterator<DailyDataPO> it = this.netHelper.getDailyData("sh000300");
//		while(it.hasNext()) stock.addDailyData(it.next());
        array.add(stock);
        return array.iterator();
    }

    public static void main(String[] args) throws NetworkConnectionException {
        StockData data = new StockData();
//		LocalDate l1 = LocalDate.of(2016, 3, 1);
//		LocalDate l2 = LocalDate.now();
//		Iterator<DailyDataPO> it = data.getStockDaily("bm000300", DateTypeConverter.convert(l1), DateTypeConverter.convert(l2));
//		while(it.hasNext())
//			it.next().print();
        data.getStockDaily("bm000300", UtilityTools.String2Cal("2016-01-01"), UtilityTools.String2Cal("2016-05-01"));
        data.getStockDaily("sh600036", UtilityTools.String2Cal("2016-01-01"), UtilityTools.String2Cal("2016-05-01"));
        data.getStockDaily("sz000031", UtilityTools.String2Cal("2016-01-01"), UtilityTools.String2Cal("2016-05-01"));
        data.getStockDaily("sh601398", UtilityTools.String2Cal("2016-01-01"), UtilityTools.String2Cal("2016-05-01"));
        data.getStockDaily("sh600000", UtilityTools.String2Cal("2016-01-01"), UtilityTools.String2Cal("2016-05-01"));
        data.getStockDaily("sh600606", UtilityTools.String2Cal("2016-01-01"), UtilityTools.String2Cal("2016-05-01"));
        data.getStockDaily("bm000300", UtilityTools.String2Cal("2016-01-01"), UtilityTools.String2Cal("2016-05-01"));

        System.out.println();


    }
}
