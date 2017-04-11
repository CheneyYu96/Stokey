package org.xeon.stockey.data.dao;

import java.util.ArrayList;
import java.util.Iterator;

import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.ResultMessage;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.data.utility.FileIOHelper;
import org.xeon.stockey.data.utility.NetworkHelper;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StockInfoPO;

public class StockInfoDAO {
	private FileIOHelper stockIOHelper;
	private FileIOHelper selectedIOHelper;
	private ArrayList<StockInfoPO> stockInfo;
	public StockInfoDAO(){
		this.stockIOHelper = new FileIOHelper("StockData.ser");
		this.selectedIOHelper = new FileIOHelper("SelectedStock.ser");
		this.stockInfo = (ArrayList<StockInfoPO>) this.stockIOHelper.readObject();
	}

	public ResultMessage addStockBat(Iterator<StockInfoPO> stocks) {
		ArrayList<StockInfoPO> tmp = new ArrayList<StockInfoPO>();
		while(stocks.hasNext()) tmp.add(stocks.next());
		this.stockIOHelper.saveObject(tmp);
		return null;
	}
	public ResultMessage addStock(StockInfoPO stock) {
		this.stockInfo.add(stock);
		this.stockIOHelper.saveObject(this.stockInfo);
		return null;
	}
	public Iterator<StockInfoPO> getAllStock() {
		ArrayList<StockInfoPO> selectedStocks =(ArrayList<StockInfoPO>)stockIOHelper.readObject();
		return selectedStocks.iterator();
	}
	public Iterator<DailyDataPO> getDailyData(String symbol){
		StockInfoPO stock = null;
		for(StockInfoPO po : stockInfo){
			if(po.getSymbol().equals(symbol)) stock = po;
		}
		//		if(stock==null)
		return null;
		//		else return stock.getAllDailyData();
	}
	public ResultMessage addDailyData(Iterator<DailyDataPO> dailyData, String symbol){
		StockInfoPO stock = null;
		for(StockInfoPO po : stockInfo){
			if(po.getSymbol().equals(symbol)) stock = po;
		}
		if(stock==null) return new ResultMessage(false,"The stock doesn't exist!");
		//		while(dailyData.hasNext())
		//			stock.addDailyData(dailyData.next());
		return new ResultMessage(true,"Success");
	}
	public Iterator<String> getStockList(){
		ArrayList<String> selectedStockList =(ArrayList<String>)selectedIOHelper.readObject();
		return selectedStockList.iterator();
	}
	public static void main1(String[] args){
		ArrayList<String> list = new ArrayList<String>();
		list.add("sh600036");
		list.add("sz002142");
		list.add("sh601398");
		list.add("sh601288");
		list.add("sh600606");
		list.add("sh600048");
		list.add("sz000031");
		list.add("sz002332");
		list.add("sz000402");
		FileIOHelper io = new FileIOHelper("SelectedStock.ser");
		io.saveObject(list);
		NetworkHelper helper = new NetworkHelper();
		ArrayList<StockInfoPO> result = new ArrayList<StockInfoPO>();
		for(String s : list){
			StockInfoPO info = helper.getStockInfo(s);
			result.add(info);
		}
		io = new FileIOHelper("StockData.ser");
		io.saveObject(result);

	}
	public static void main(String[] args) throws NetworkConnectionException{
		StockDataService data = DataServiceFactory.getStockDataService();
		Iterator<StockInfoPO> it = data.getAllStock();
		while(it.hasNext()) it.next().print();
	}
}
