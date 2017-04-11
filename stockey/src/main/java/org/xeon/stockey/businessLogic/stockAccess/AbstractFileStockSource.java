package org.xeon.stockey.businessLogic.stockAccess;

import org.xeon.stockey.businessLogic.utility.*;
import org.xeon.stockey.businessLogicService.stockAccessService.StockDailyDataFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockInfoFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StockInfoPO;
import org.xeon.stockey.util.R;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Sissel on 2016/3/12.
 */
public abstract class AbstractFileStockSource implements StockSourceService
{
    protected static StockDataService stockDataService;
    protected abstract Map<String, StockInfoVO> stockInfoMap() throws NetworkConnectionException;

    @Override
    public Collection<StockInfoVO> getAllStockInfo() throws NetworkConnectionException
    {
        return stockInfoMap().values();
    }

    @Override
    public Collection<StockInfoVO> filterStockInfo(Collection<Filter> filters) throws NetworkConnectionException
    {
        return ManualFilterHelper.filterStockInfo(
                stockInfoMap().values(),
                filters);
    }

    @Override
    public Collection<StockInfoVO> filterStockInfo(Filter...filters) throws NetworkConnectionException
    {
        return ManualFilterHelper.filterStockInfo(
                stockInfoMap().values(),
                Arrays.asList(filters));
    }

    @Override
    public Collection<StockInfoVO> filterStockInfo(StockInfoFilterService stockInfoFilterService)
            throws NetworkConnectionException
    {
        return stockInfoFilterService.getResult();
    }

    @Override
    public StockInfoVO getStockInfo(String stockCode) throws NetworkConnectionException
    {
        return stockInfoMap().get(stockCode);
    }

    @Override
    public abstract Collection<DailyDataVO> getAllStockDailyData(String stockCode) throws NetworkConnectionException;

    @Override
    public Collection<DailyDataVO> getAllStockDailyData(StockInfoVO stockInfoVO) throws NetworkConnectionException
    {
        return getAllStockDailyData(stockInfoVO.getStockCode());
    }

    @Override
    public Collection<DailyDataVO> filterStockDailyData(StockInfoVO stockInfoVO, Collection<Filter> filters)
            throws NetworkConnectionException
    {
        Collection<DailyDataVO> first = getAllStockDailyData(stockInfoVO);
        return ManualFilterHelper.filterDailyData(first, filters);
    }

    @Override
    public Collection<DailyDataVO> filterStockDailyData(StockInfoVO stockInfoVO, Filter...filters)
            throws NetworkConnectionException
    {
        Collection<DailyDataVO> origin = getAllStockDailyData(stockInfoVO);
        return ManualFilterHelper.filterDailyData(origin, Arrays.asList(filters));
    }

    @Override
    public Collection<DailyDataVO> filterStockDailyData(StockDailyDataFilterService stockDailyDataFilterService)
            throws NetworkConnectionException
    {
        return stockDailyDataFilterService.getResult();
    }

    @Override
    public DailyDataVO getLatestDailyData(String stockCode) throws NetworkConnectionException
    {
        LocalDate nowDate = LocalDate.now();
        Iterator<DailyDataPO> iterator = stockDataService
                .getStockDaily(stockCode,
                        DateTypeConverter.convert(nowDate.minusMonths(1)),
                        DateTypeConverter.convert(nowDate));

        DailyDataPO last = null;
        while (iterator.hasNext())
        {
            last = iterator.next();
        }

        return new DailyDataVO(last);
    }
}
