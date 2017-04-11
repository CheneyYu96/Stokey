package org.xeon.stockey.businessLogic.stockAccess;

import org.hibernate.annotations.Source;
import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
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

import java.util.*;

/**
 * Created by Sissel on 2016/3/9.
 * StockSourceService 在源是数据库(StockDataService)时候的实现
 */
public class DBStockSourceImpl implements StockSourceService
{
    private StockDataService stockDataService = DataServiceFactory.getStockDataService();

    @Override
    public Collection<StockInfoVO> getAllStockInfo() throws NetworkConnectionException
    {
        // if filters is null, means I can get all the stockInfo without filtering
        Iterator<StockInfoPO> queryResults = stockDataService.getAllStock();
        return SourceDataHelper.transStockResult(queryResults);
    }

    @Override
    public Collection<StockInfoVO> filterStockInfo(Collection<Filter> filters) throws NetworkConnectionException
    {
        Iterator<StockInfoPO> stockInfoIter = stockDataService.searchStockInfo(filters.iterator());
        return SourceDataHelper.transStockResult(stockInfoIter);
    }

    @Override
    public Collection<StockInfoVO> filterStockInfo(Filter...filters) throws NetworkConnectionException
    {
        List<Filter> filterList = Arrays.asList(filters);
        return this.filterStockInfo(filterList);
    }

    @Override
    public Collection<StockInfoVO> filterStockInfo(StockInfoFilterService stockInfoFilterService) throws NetworkConnectionException
    {
        return stockInfoFilterService.getResult();
    }

    @Override
    public StockInfoVO getStockInfo(String stockCode) throws NetworkConnectionException
    {
        Filter filter = new Filter(Filter.FieldType.symbol, String.class, Filter.CompareType.EQ, stockCode);
        List<Filter> filters = new LinkedList<>();
        filters.add(filter);
        Iterator<StockInfoVO> iterator = filterStockInfo(filters).iterator();
        // if can't find match result, return null
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public Collection<StockInfoVO> getStockInfos(List<String> stockCode) throws NetworkConnectionException {
        Collection<StockInfoVO> results = new ArrayList<>();
        Iterator<String> iterator = stockCode.iterator();
        while (iterator.hasNext()){
            results.add(getStockInfo(iterator.next()));
        }
        return results;

    }


    @Override
    public Collection<DailyDataVO> getAllStockDailyData(String stockCode)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<DailyDataVO> getAllStockDailyData(StockInfoVO stockInfoVO)
    {
        return this.getAllStockDailyData(stockInfoVO.getStockCode());
    }

    @Override
    public Collection<DailyDataVO> filterStockDailyData(StockInfoVO stockInfoVO, Collection<Filter> filters)
    {
        String rawNum = stockInfoVO.getStockCode().substring(2);
        Filter filter = new Filter(Filter.FieldType.id, String.class, Filter.CompareType.START_WITH, rawNum);
        filters.add(filter);
        Iterator<DailyDataPO> dailyDataIterator = stockDataService.searchStock(filters.iterator());
        return SourceDataHelper.transDailyResult(dailyDataIterator);
    }

    @Override
    public Collection<DailyDataVO> filterStockDailyData(StockInfoVO stockInfoVO, Filter...filters)
    {
        List<Filter> filterList = Arrays.asList(filters);
        return this.filterStockDailyData(stockInfoVO, filterList);
    }

    @Override
    public Collection<DailyDataVO> filterStockDailyData(StockDailyDataFilterService stockDailyDataFilterService)
            throws NetworkConnectionException
    {
        return stockDailyDataFilterService.getResult();
    }

    @Override
    public DailyDataVO getLatestDailyData(String stockCode)
    {
        DailyDataPO dailyDataPO = stockDataService.getLatestDailyData(stockCode);
        return dailyDataPO == null ? null : new DailyDataVO(dailyDataPO);
    }
}
