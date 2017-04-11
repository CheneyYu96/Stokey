package org.xeon.stockey.businessLogic.stockAccess;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.SourceDataHelper;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StockInfoPO;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * Created by Sissel on 2016/3/12.
 * StockSourceService在源是File且需要的是大盘时候的实现
 */
//@RestController
//@RequestMapping("/access/benchmark")
public class FileBenchmarkSourceImpl extends AbstractFileStockSource
{
    protected static Map<String, StockInfoVO> stockInfoMap = null;
    private static Map<String, List<DailyDataPO>> dailyMap = new HashMap<>();

    static
    {
        stockDataService = DataServiceFactory.getStockDataService();
    }

    private static void loadData() throws NetworkConnectionException
    {
        stockInfoMap = new HashMap<>();

        // load data from files
        Iterator<StockInfoPO> stockInfoIter = stockDataService.getBenchmark();

        while (stockInfoIter.hasNext())
        {
            StockInfoVO stockInfoVO = new StockInfoVO(stockInfoIter.next());
            stockInfoMap.put(stockInfoVO.getStockCode(), stockInfoVO);
        }
    }

    @Override
    protected synchronized Map<String, StockInfoVO> stockInfoMap() throws NetworkConnectionException
    {
        if(stockInfoMap == null)
        {
            loadData();
        }

        return stockInfoMap;
    }

    @Override
    public Collection<StockInfoVO> getStockInfos(List<String> stockCode) throws NetworkConnectionException {
        return null;
    }

    @Override
    @RequestMapping("/getAllStockDailyData")
    public synchronized Collection<DailyDataVO> getAllStockDailyData(
    		@RequestParam("stockCode") String stockCode
    		) throws NetworkConnectionException
    {
        if (dailyMap.get(stockCode) == null)
        {
            Calendar begin = Calendar.getInstance();
            begin.set(2014, Calendar.JANUARY, 1);
            Calendar end = Calendar.getInstance();

            Iterator<DailyDataPO> iterator = stockDataService.getStockDaily(stockCode, begin, end);
            List<DailyDataPO> poCache = HolidayFilter.filterFromIterator(iterator);

            dailyMap.put(stockCode, poCache);
        }

        return SourceDataHelper.transDailyResult(dailyMap.get(stockCode).iterator());
    }
}
