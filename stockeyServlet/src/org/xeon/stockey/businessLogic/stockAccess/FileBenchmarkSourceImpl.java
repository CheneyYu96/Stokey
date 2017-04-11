package org.xeon.stockey.businessLogic.stockAccess;

import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.ResultMessage;
import org.xeon.stockey.businessLogic.utility.SourceDataHelper;
import org.xeon.stockey.businessLogicService.stockAccessService.StockDailyDataFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockInfoFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.data.impl.StockData;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StockInfoPO;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by Sissel on 2016/3/12.
 * StockSourceService在源是File且需要的是大盘时候的实现
 */
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
    protected Map<String, StockInfoVO> stockInfoMap() throws NetworkConnectionException
    {
        if(stockInfoMap == null)
        {
            loadData();
        }

        return stockInfoMap;
    }

    @Override
    public Collection<DailyDataVO> getAllStockDailyData(String stockCode) throws NetworkConnectionException
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
