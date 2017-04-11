package org.xeon.stockey.dataService;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.po.StockInfoPO;

public class StockDataServiceTest{
	StockDataService stockData;
	
	public StockDataServiceTest(){
		stockData = DataServiceFactory.getStockDataService();
	}
	@Test
	public void getBenchmarkTest() throws NetworkConnectionException {
		Iterator<StockInfoPO> it = stockData.getBenchmark();
		Assert.assertEquals(it.next().getMarket(),"BENCHMARK");
	}

	@Test
	public void getAllStockTest() throws NetworkConnectionException {
		Iterator<StockInfoPO> it = stockData.getAllStock();
		int num = 0;
		while(it.hasNext()){
			num++; it.next();
		}
		Assert.assertEquals(num, 9);
	}

	@Test
	public void getStockDailyTest() throws NetworkConnectionException {
		Iterator<DailyDataPO> it = stockData.getStockDaily("sh600036",null,null);
		Assert.assertEquals(it.next().getId().substring(0, 6), "600036");

	}
}
