package org.xeon.stockey.businessLogicService.stockAccessService;

import org.junit.Assert;
import org.junit.Test;
import org.xeon.stockey.businessLogic.stockAccess.DBStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StockSourceService接口的测试
 * Created by Sissel on 2016/4/16.
 */
public class StockSourceServiceTest
{
    StockSourceService stockSourceService = new DBStockSourceImpl();

    @Test
    public void testGetAllStockInfo() throws Exception
    {
        Collection<StockInfoVO> stockInfoVOs = stockSourceService.getAllStockInfo();
        String[] expectedStockCodes = { "sh601398", "sh601288", "sh600606", "sz002332", "sz002142", "sz000031",
        "sh600048", "sh600036", "sz000402"};
        List<String> gotStockCodes = stockInfoVOs.stream().map(StockInfoVO::getStockCode).collect(Collectors.toList());

        Assert.assertEquals(expectedStockCodes.length, gotStockCodes.size());
        for (String expectedStockCode : expectedStockCodes)
        {
            Assert.assertTrue(gotStockCodes.contains(expectedStockCode));
        }
    }

    @Test
    public void testGetStockInfo() throws Exception
    {
        StockInfoVO stockInfoVO1 = stockSourceService.getStockInfo("sh601398");
        Assert.assertEquals("sh601398", stockInfoVO1.getStockCode());

        StockInfoVO stockInfoVO2 = stockSourceService.getStockInfo("sh600606");
        Assert.assertEquals("sh600606", stockInfoVO2.getStockCode());

        StockInfoVO stockInfoVO3 = stockSourceService.getStockInfo("sz002142");
        Assert.assertEquals("sz002142", stockInfoVO3.getStockCode());
    }

    @Test
    public void testFilterStockInfo() throws Exception
    {
        Filter filter = new Filter(Filter.FieldType.id, String.class, Filter.CompareType.EQ, "sh600606");
        Collection<StockInfoVO> stockInfoVOs = stockSourceService.filterStockInfo(filter);

        Assert.assertEquals(1, stockInfoVOs.size());
        Assert.assertEquals(
                "sh600606",
                stockInfoVOs.stream().findFirst().get().getStockCode());
    }

    @Test
    public void testFilterStockDailyData() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        Filter filter1 = new Filter(Filter.FieldType.theDate, LocalDate.class, Filter.CompareType.LET, end);
        Filter filter2 = new Filter(Filter.FieldType.theDate, LocalDate.class, Filter.CompareType.BET, begin);

        StockInfoVO stockInfoVO = stockSourceService.getStockInfo("sh601398");
        Collection<DailyDataVO> dailyDataVOs = stockSourceService.filterStockDailyData(stockInfoVO, filter1, filter2);

        Assert.assertEquals(5, dailyDataVOs.size());
        LocalDate expected = begin;
        for (DailyDataVO dailyDataVO : dailyDataVOs)
        {
            Assert.assertEquals(expected, dailyDataVO.getDate());
            expected = expected.plusDays(1);
        }
    }
}