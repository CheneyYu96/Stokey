package org.xeon.stockey.businessLogicService.stockAccessService;

import org.junit.Assert;
import org.junit.Test;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockInfoFilterImpl;
import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * StockInfoFilterService接口的测试
 * Created by Sissel on 2016/4/16.
 */
public class StockInfoFilterServiceTest
{
    StockSourceService stockSourceService = new FileStockSourceImpl();
    StockInfoFilterService stockInfoFilterService = new StockInfoFilterImpl(stockSourceService);

    @Test
    public void testFilterStockCode() throws Exception
    {
        Collection<StockInfoVO> stockInfoVOs = stockInfoFilterService.filterStockCode("sh601398").getResult();

        Assert.assertEquals(1, stockInfoVOs.size());
        Assert.assertEquals(
                "sh601398",
                stockInfoVOs.stream().findFirst().get().getStockCode()
        );
    }

    @Test
    public void testFilterExchange() throws Exception
    {
        Collection<String> gotStockCodes = stockInfoFilterService
                .filterExchange(ExchangeEnum.SHANGHAI)
                .getResult()
                .stream().map(StockInfoVO::getStockCode).collect(Collectors.toList());

        String[] expectedStockCodes = { "sh601398", "sh601288", "sh600606", "sh600048", "sh600036"};
        Assert.assertEquals(expectedStockCodes.length, gotStockCodes.size());
        for (String expectedStockCode : expectedStockCodes)
        {
            Assert.assertTrue(gotStockCodes.contains(expectedStockCode));
        }
    }
}