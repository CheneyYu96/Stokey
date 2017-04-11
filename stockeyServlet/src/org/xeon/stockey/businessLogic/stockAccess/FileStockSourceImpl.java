package org.xeon.stockey.businessLogic.stockAccess;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.*;

import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.SourceDataHelper;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StockInfoPO;
import org.xeon.stockey.util.R;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * Created by Sissel on 2016/3/10.
 * StockSourceService 在源是文件时候的实现
 */
public class FileStockSourceImpl extends AbstractFileStockSource
{
    private static Map<String, StockInfoVO> stockInfoMap = null;
    private static Map<String, List<DailyDataPO>> dailyMap = new HashMap<>();

    static
    {
        stockDataService = DataServiceFactory.getStockDataService();
    }

    // used to generate test samples
    public static void main(String[] args) throws IOException
    {
        FileStockSourceImpl source = new FileStockSourceImpl();
        Map<String, StockInfoVO> tempStockMap = new HashMap<>();
        Map<String, Map<LocalDate, DailyDataVO>> tempDailyMap =  new HashMap<>();;

        Collection<StockInfoVO> stockInfoVOs = source.stockInfoMap.values();
        Iterator<StockInfoVO> iterator = stockInfoVOs.iterator();
        int i = 0;
        while (i < 6)
        {
            StockInfoVO stock = iterator.next();
            if(stock.getStockCode() != null)
            {
                tempStockMap.put(stock.getStockCode(), stock);

                //Map<LocalDate, DailyDataVO> map = source.dailyDataMap.get(stock.getStockCode());
                //tempDailyMap.put(stock.getStockCode(), map);
            }

            ++i;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(R.path.testStockInfoPath)))
        {
            oos.writeObject(tempStockMap);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(R.path.testDailyDataPath)))
        {
            oos.writeObject(tempDailyMap);
        }
    }

    @Override
    protected Map<String, StockInfoVO> stockInfoMap() throws NetworkConnectionException
    {
        if (stockInfoMap == null)
        {
            stockInfoMap = new HashMap<>();

            // load data from files
            Iterator<StockInfoPO> stockInfoIter = stockDataService.getAllStock();

            while (stockInfoIter.hasNext())
            {
                StockInfoVO stockInfoVO = new StockInfoVO(stockInfoIter.next());
                stockInfoMap.put(stockInfoVO.getStockCode(), stockInfoVO);
            }
        }

        return stockInfoMap;
    }

    @Override
    public Collection<DailyDataVO> getAllStockDailyData(String stockCode) throws NetworkConnectionException
    {
        if (dailyMap.get(stockCode) == null)
        {
            Calendar begin = Calendar.getInstance();
            begin.set(2000, Calendar.JANUARY, 1);
            Calendar end = Calendar.getInstance();

            Iterator<DailyDataPO> iterator = stockDataService.getStockDaily(stockCode, begin, end);
            List<DailyDataPO> poCache = HolidayFilter.filterFromIterator(iterator);

            dailyMap.put(stockCode, poCache);
        }

        return SourceDataHelper.transDailyResult(dailyMap.get(stockCode).iterator());
    }
}
