package org.xeon.stockey.businessLogicService.stockAccessService;

import org.junit.Assert;
import org.junit.Test;
import org.xeon.stockey.businessLogic.stockAccess.DBStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockDailyDataFilterImpl;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * StockDailyDataFilterService接口的测试
 * Created by Sissel on 2016/4/16.
 */
public class StockDailyDataFilterServiceTest
{
    StockSourceService stockSourceService = new DBStockSourceImpl();
    StockDailyDataFilterService filterService = new StockDailyDataFilterImpl(stockSourceService);

    @Test
    public void testFilterDate() throws Exception
    {
        StockInfoVO stockInfoVO = stockSourceService.getStockInfo("sh601398");
        filterService.setStockInfoVO(stockInfoVO);

        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        Collection<DailyDataVO> dailyDataVOs = filterService.filterDate(begin, end).getResult();

        Assert.assertEquals(5, dailyDataVOs.size());
        LocalDate expected = begin;
        for (DailyDataVO dailyDataVO : dailyDataVOs)
        {
            Assert.assertEquals(expected, dailyDataVO.getDate());
            expected = expected.plusDays(1);
        }
    }

    @Test
    public void testFilterOpen() throws Exception
    {
        StockInfoVO stockInfoVO = stockSourceService.getStockInfo("sh601398");
        filterService.setStockInfoVO(stockInfoVO);

        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 14);
        Collection<DailyDataVO> dailyDataVOs = filterService.filterDate(begin, end).filterOpen(4.5, 4.6).getResult();

        Assert.assertEquals(1, dailyDataVOs.size());
    }

    @Test
    public void testFilterHigh() throws Exception
    {
        StockInfoVO stockInfoVO = stockSourceService.getStockInfo("sh601398");
        filterService.setStockInfoVO(stockInfoVO);

        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 14);
        Collection<DailyDataVO> dailyDataVOs = filterService.filterDate(begin, end).filterHigh(4.5, 4.6).getResult();

        Assert.assertEquals(4, dailyDataVOs.size());
    }

    @Test
    public void testFilterLow() throws Exception
    {
        StockInfoVO stockInfoVO = stockSourceService.getStockInfo("sh601398");
        filterService.setStockInfoVO(stockInfoVO);

        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 14);
        Collection<DailyDataVO> dailyDataVOs = filterService.filterDate(begin, end).filterLow(4.2, 4.3).getResult();

        Assert.assertEquals(2, dailyDataVOs.size());
    }

    @Test
    public void testFilterClose() throws Exception
    {
        StockInfoVO stockInfoVO = stockSourceService.getStockInfo("sh601398");
        filterService.setStockInfoVO(stockInfoVO);

        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 14);
        Collection<DailyDataVO> dailyDataVOs = filterService.filterDate(begin, end).filterClose(4.3, 4.4).getResult();

        Assert.assertEquals(4, dailyDataVOs.size());
    }

    @Test
    public void testFilterVolume() throws Exception
    {
        StockInfoVO stockInfoVO = stockSourceService.getStockInfo("sh601398");
        filterService.setStockInfoVO(stockInfoVO);

        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 14);
        Collection<DailyDataVO> dailyDataVOs = filterService
                .filterDate(begin, end)
                .filterVolume(140000000, 160000000)
                .getResult();

        Assert.assertEquals(1, dailyDataVOs.size());
    }

    @Test
    public void testFilterTurnover() throws Exception
    {
        StockInfoVO stockInfoVO = stockSourceService.getStockInfo("sh601398");
        filterService.setStockInfoVO(stockInfoVO);

        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 14);
        Collection<DailyDataVO> dailyDataVOs = filterService.filterDate(begin, end).filterTurnover(0.05, 0.06).getResult();

        Assert.assertEquals(4, dailyDataVOs.size());
    }
}