package org.xeon.stockey.data.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.xeon.stockey.businessLogic.utility.DateTypeConverter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.ResultMessage;
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

	StockData(){
		this.stockDao = new StockInfoDAO();
		this.dailyDao = new DailyDataDAO();
		this.netHelper = new NetworkHelper();
	}

	@Override
	public Iterator<DailyDataPO> getStockDaily(String symbol, Calendar startDate, Calendar endDate) throws NetworkConnectionException {
		Iterator<DailyDataPO> it = netHelper.getDailyData(symbol, startDate, endDate);
		if(it==null) throw new NetworkConnectionException();
		ArrayList<DailyDataPO> tmp = new ArrayList<DailyDataPO>();
		while(it.hasNext()) tmp.add(it.next());
		for(int i=0;i<tmp.size();i++){
			DailyDataPO po = tmp.get(i);
			if(po.getHigh()*po.getClose()*po.getOpen()*po.getLow()==0){
				DailyDataPO pre = null;
				DailyDataPO post = null;
				if(i!=0) pre = tmp.get(i-1);
				if(i!=tmp.size()) post = tmp.get(i+1);
				if(post!=null&&post.getHigh()*post.getClose()*post.getOpen()*post.getLow()!=0){
					po.setClose(post.getClose());
					po.setHigh(post.getClose());
					po.setLow(post.getClose());
					po.setOpen(post.getClose());
				}
				if(pre!=null&&pre.getHigh()*pre.getClose()*pre.getOpen()*pre.getLow()!=0){
					po.setClose(pre.getClose());
					po.setHigh(pre.getClose());
					po.setLow(pre.getClose());
					po.setOpen(pre.getClose());
				}
			}
		}
		return tmp.iterator();
	}

	@Override
	public ResultMessage addStockBat(Iterator<StockInfoPO> stocks) throws NetworkConnectionException {
		return stockDao.addStockBat(stocks);
	}

	@Override
	public Iterator<StockInfoPO> getAllStock() throws NetworkConnectionException {
		return stockDao.getAllStock();
//		Iterator<String> it = stockDao.getStockList();
//		ArrayList<StockInfoPO> stockInfoList = new ArrayList<StockInfoPO>();
//		while(it.hasNext()){
//			StockInfoPO stock = netHelper.getStockInfo(it.next());
//			stockInfoList.add(stock);
//		}
//		return stockInfoList.iterator();
	}

	@Override
	public Iterator<StockInfoPO> getBenchmark() throws NetworkConnectionException {
		ArrayList<StockInfoPO> array = new ArrayList<StockInfoPO>();
		StockInfoPO stock = new StockInfoPO();
		stock.setMarket(ExchangeEnum.BENCHMARK);
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
	public static void main(String[] args) throws NetworkConnectionException{
		StockData data = new StockData();
		LocalDate l1 = LocalDate.of(2016, 3, 1);
		LocalDate l2 = LocalDate.now();
		Iterator<DailyDataPO> it = data.getStockDaily("bm000300", DateTypeConverter.convert(l1), DateTypeConverter.convert(l2));
		while(it.hasNext())
			it.next().print();
		
		
	}
}
