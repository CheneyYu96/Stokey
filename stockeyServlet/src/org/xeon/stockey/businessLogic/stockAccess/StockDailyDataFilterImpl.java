package org.xeon.stockey.businessLogic.stockAccess;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.ManualFilterHelper;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockDailyDataFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * Created by Sissel on 2016/3/8.
 * StockDailyDataFilterService的一种lazy实现
 */
public class StockDailyDataFilterImpl implements StockDailyDataFilterService
{
    private StockInfoVO stockInfo;
    private StockSourceService stockSource;
    private Set<Filter> filters;

    public StockDailyDataFilterImpl() 
    {
    	filters = new HashSet<>();
	}
    
    public StockDailyDataFilterImpl(StockInfoVO stockInfo)
    {
    	this();
        this.stockInfo = stockInfo;
    }

    public StockDailyDataFilterImpl(StockInfoVO stockInfo, StockSourceService stockSource)
    {
        this(stockInfo);
        this.stockSource = stockSource;
    }

    public StockDailyDataFilterImpl(StockSourceService dbStockSourceImpl) {
    	this();
    	stockSource = dbStockSourceImpl;
	}

	public Collection<DailyDataVO> getResult() throws NetworkConnectionException
    {
        return stockSource.filterStockDailyData(stockInfo, filters);
    }

    public StockDailyDataFilterService setStockInfoVO(StockInfoVO stockInfoVO)
    {
        stockInfo = stockInfoVO;
        return this;
    }

    public StockInfoVO getStockInfoVO()
    {
        return stockInfo;
    }

    public StockDailyDataFilterService setStockSource(StockSourceService stockSource)
    {
        this.stockSource = stockSource;
        return this;
    }

    public StockSourceService getStockSource()
    {
        return stockSource;
    }

    public StockDailyDataFilterService filterDate(LocalDate begin, LocalDate end)
    {
        Filter filter = new Filter(Filter.FieldType.date, LocalDate.class, Filter.CompareType.LET, end);
        filters.add(filter);
        Filter another = new Filter(Filter.FieldType.date, LocalDate.class, Filter.CompareType.BET, begin);
        filters.add(another);
        return this;
    }

    private void addNumFilter(Filter.FieldType fieldType, double lowerBound, double upperBound)
    {
        Filter filter = new Filter(fieldType, Double.class, Filter.CompareType.LET, upperBound);
        filters.add(filter);
        Filter another = new Filter(fieldType, Double.class, Filter.CompareType.BET, lowerBound);
        filters.add(another);
        filters.add(filter);
    }

    public StockDailyDataFilterService filterOpen(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.open, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService filterHigh(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.high, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService filterLow(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.low, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService filterClose(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.close, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService filterAdj_Price(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.adj_price, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService filterVolume(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.volume, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService filterTurnover(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.turnover, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService filterPE(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.pe, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService filterPB(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.pb, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService addFilter(Filter filter)
    {
        filters.add(filter);
        return this;
    }

    public StockDailyDataFilterService removeFilter(Filter filter)
    {
        filters.remove(filter);
        return this;
    }

    public StockDailyDataFilterService clearFilters()
    {
        filters.clear();
        return this;
    }

    // TODO: 2016/3/9
    public StockDailyDataFilterService union(StockDailyDataFilterService another)
    {
        return null;
    }

    // TODO: 2016/3/9
    public StockDailyDataFilterService join(StockDailyDataFilterService another)
    {
        return null;
    }

    // TODO: 2016/3/9
    public StockDailyDataFilterService subtract(StockDailyDataFilterService another)
    {
        return null;
    }
}
