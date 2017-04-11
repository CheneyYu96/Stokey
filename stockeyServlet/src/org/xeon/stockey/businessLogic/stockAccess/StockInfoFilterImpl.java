package org.xeon.stockey.businessLogic.stockAccess;

import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockInfoFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.vo.StockInfoVO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sissel on 2016/3/8.
 * StockInfoFilterService的一种lazy实现
 */
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

    public Collection<StockInfoVO> getResult() throws NetworkConnectionException
    {
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

    public StockInfoFilterService filterStockCode(String stockCode)
    {
        Filter filter = new Filter(
                Filter.FieldType.stockCode,
                String.class,
                Filter.CompareType.EQ,
                stockCode);
        filters.add(filter);
        return this;
    }

    public StockInfoFilterService filterExchange(ExchangeEnum exchangeEnum)
    {
        Filter filter = new Filter(
                Filter.FieldType.exchange,
                Filter.DataType.EXCHANGE_ENUM.getClass(),
                Filter.CompareType.EQ,
                exchangeEnum);
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
