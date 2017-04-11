package org.xeon.stockey.businessLogic.stockAccess;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.xeon.stockey.businessLogic.utility.DateTypeConverter;
import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.businessLogicService.stockAccessService.StockDailyDataFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * Created by Sissel on 2016/3/8.
 * StockDailyDataFilterService的一种lazy实现
 */
//@RestController
//@RequestMapping("/access/daily")
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
        this.setStockInfoVO(stockInfo);
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
    
    @RequestMapping("/getResult")
	public Collection<DailyDataVO> getResult() throws NetworkConnectionException
    {
        return stockSource.filterStockDailyData(stockInfo, filters);
    }

    @RequestMapping("/setStockInfoVO")
    public StockDailyDataFilterService setStockInfoVO(StockInfoVO stockInfoVO)
    {
        this.stockInfo = stockInfoVO;
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
        Calendar beginCalendar = DateTypeConverter.convert(begin);
        System.out.println(UtilityTools.Cal2String(beginCalendar));
        Calendar endCalendar = DateTypeConverter.convert(end);
        System.out.println(UtilityTools.Cal2String(endCalendar));

        Filter filter = new Filter(Filter.FieldType.theDate, Calendar.class, Filter.CompareType.LET, endCalendar);
        filters.add(filter);
        Filter another = new Filter(Filter.FieldType.theDate, Calendar.class, Filter.CompareType.BET, beginCalendar);
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
        addNumFilter(Filter.FieldType.adjPrice, lowerBound, upperBound);
        return this;
    }

    public StockDailyDataFilterService filterVolume(double lowerBound, double upperBound)
    {
        addNumFilter(Filter.FieldType.volumn, lowerBound, upperBound);
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
}
