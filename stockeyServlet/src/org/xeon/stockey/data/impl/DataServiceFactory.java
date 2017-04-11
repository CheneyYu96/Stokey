package org.xeon.stockey.data.impl;

import org.xeon.stockey.dataService.StockDataService;

public class DataServiceFactory {
	private static StockDataService stockData;
	public static StockDataService getStockDataService(){
		if(stockData==null) stockData = new StockData();
		return stockData;
	}
	
}
