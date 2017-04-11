package org.xeon.stockey.businessLogic.stockAccess;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockInfoFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.vo.StockInfoVO;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sissel on 2016/3/8.
 * StockInfoFilterService的一种lazy实现
 */
@RestController
@RequestMapping("/access/info")
public class StockInfoFilterImpl implements StockInfoFilterService
{
    private StockSourceService stockSource;
    private Set<Filter> filters;

    public StockInfoFilterImpl()
    {
        filters = new HashSet<Filter>();
    }

    public StockInfoFilterImpl(StockSourceService stockSource)
    {
        this();
        this.stockSource = stockSource;
    }

    @RequestMapping("/getResult")
    public Collection<StockInfoVO> getResult() throws NetworkConnectionException
    {
        System.err.println("before filter Stock Info @" + Calendar.getInstance().getTimeInMillis());
        return stockSource.filterStockInfo(filters);
    }

    public StockInfoFilterService setStockSource(StockSourceService stockSource)
    {
        this.stockSource = stockSource;
        return this;
    }

    public StockSourceService getStockSource()
    {
        return stockSource;
    }

    @RequestMapping("/filterStockCode")
    public StockInfoFilterService filterStockCode(
            @RequestParam("stockCode") String stockCode)
    {
        System.err.println("before filter @ " + Calendar.getInstance().getTimeInMillis());
        Filter filter = new Filter(
                Filter.FieldType.symbol,
                String.class,
                Filter.CompareType.EQ,
                stockCode);
        filters.add(filter);
        return this;
    }

    public StockInfoFilterService filterExchange(ExchangeEnum exchangeEnum)
    {
        Filter filter = new Filter(
                Filter.FieldType.market,
                Filter.DataType.STRING.getClass(),
                Filter.CompareType.EQ,
                exchangeEnum.name());
        filters.add(filter);
        return this;
    }

    public StockInfoFilterService filterIndustry(String industry)
    {
        Filter filter = new Filter(
                Filter.FieldType.type,
                Filter.DataType.STRING.getClass(),
                Filter.CompareType.EQ,
                industry);
        filters.add(filter);
        return this;
    }

    @Override
    public StockInfoFilterService filterName(String name) {
        Filter filter = new Filter(
                Filter.FieldType.name,
                Filter.DataType.STRING.getClass(),
                Filter.CompareType.EQ,
                name);
        filters.add(filter);
        return this;
    }

    public StockInfoFilterService addFilter(Filter filter)
    {
        filters.add(filter);
        return this;
    }

    public StockInfoFilterService removeFilter(Filter filter)
    {
        // match by equals() defined in Filter
        filters.remove(filter);
        return this;
    }

    public StockInfoFilterService clearFilters()
    {
        filters.clear();
        return this;
    }
}
