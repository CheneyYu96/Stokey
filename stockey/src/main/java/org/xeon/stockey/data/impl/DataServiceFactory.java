package org.xeon.stockey.data.impl;

import org.xeon.stockey.dataService.ConActDataService;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.dataService.UserDataService;

public class DataServiceFactory {
	private static MySessionFactory sessionFactory;
	private static StockDataService stockData;
    private static UserDataService userData;
    private static ConActDataService conActData;


	public static StockDataService getStockDataService(){
		DataServiceFactory.ensureSession();
		
		if(stockData==null) stockData = new StockData();
		return stockData;
	}

    public static UserDataService getUserDataService(){
        DataServiceFactory.ensureSession();

        if(userData==null) userData = new UserData();
        return userData;
    }

    public static ConActDataService getConActDataService(){
        DataServiceFactory.ensureSession();

        if(conActData==null) conActData = new ConActData();
        return conActData;
    }
	
	private static void ensureSession(){
		if(DataServiceFactory.sessionFactory==null)
			DataServiceFactory.sessionFactory = new MySessionFactory();
	}
	
}
