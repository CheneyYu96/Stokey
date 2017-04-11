package org.xeon.stockey.businessLogic.utility;

import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Sissel on 2016/3/11.
 * 为了重用 手动筛选 部分的代码而设
 */
public class ManualFilterHelper
{
    private static boolean compareObj(Object actual, Object standard, Class type, Filter.CompareType compareType)
    {
        if(actual == null || standard == null)
        {
            System.err.println("ManualFilterHelper.compareObj: comparing null value");
            return false;
        }

        switch (compareType)
        {
            case EQ:
                if(type == String.class || type.isEnum() || type == LocalDate.class)
                {
                    return actual.equals(standard);
                }
                else
                {
                    System.err.println("ManualFilterHelper.compareObj: EQ, not found type:" + type.getName());
                    return actual.equals(standard);
                }
            default:
                if(type == Double.class)
                {
                    double actualD = (Double)actual;
                    double standardD = (Double)standard;
                    if(compareType == Filter.CompareType.LET)
                    {
                        return actualD <= standardD;
                    }
                    else if(compareType == Filter.CompareType.BET)
                    {
                        return actualD >= standardD;
                    }
                    else if(compareType == Filter.CompareType.LT)
                    {
                        return actualD < standardD;
                    }
                    else // BT
                    {
                        return actualD > standardD;
                    }
                }
                else if(type == Integer.class)
                {
                    int actualI = (Integer) actual;
                    int standardI = (Integer) standard;
                    if(compareType == Filter.CompareType.LET)
                    {
                        return actualI <= standardI;
                    }
                    else if(compareType == Filter.CompareType.BET)
                    {
                        return actualI >= standardI;
                    }
                    else if(compareType == Filter.CompareType.LT)
                    {
                        return actualI < standardI;
                    }
                    else // BT
                    {
                        return actualI > standardI;
                    }
                }
                else if(type == LocalDate.class)
                {
                    LocalDate actualLD = (LocalDate)actual;
                    LocalDate standardLD = (LocalDate)standard;
                    if(compareType == Filter.CompareType.LET)
                    {
                        return actualLD.compareTo(standardLD) <= 0;
                    }
                    else if(compareType == Filter.CompareType.BET)
                    {
                        return actualLD.compareTo(standardLD) >= 0;
                    }
                    else if(compareType == Filter.CompareType.LT)
                    {
                        return actualLD.compareTo(standardLD) < 0;
                    }
                    else // BT
                    {
                        return actualLD.compareTo(standardLD) > 0;
                    }
                }
                else
                {
                    System.err.println("ManualFilterHelper.compareObj: NOT EQ,not found type:" + type.getName());
                    return false;
                }
        }
    }

    private static boolean canSatisfy(StockInfoVO stockInfo, Filter filter)
    {
        Object actualObj = null;
        switch (filter.fieldType)
        {
            case id:
                actualObj = stockInfo.getStockCode();
                break;
            case market:
                actualObj = stockInfo.getMarket();
                break;
            default:
                System.err.println("ManualFilterHelper.canSatisfy: missing fieldType");
                break;
        }

        return compareObj(actualObj, filter.value, filter.dataType, filter.compareType);
    }

    private static boolean canSatisfy(DailyDataVO dailyData, Filter filter)
    {
        Object actualObj = null;
        switch (filter.fieldType)
        {
            case id:
                break;
            case market:
                break;
            case year:
                actualObj = dailyData.getDate().getYear();
                break;
            case theDate:
                actualObj = dailyData.getDate();
                break;
            case open:
                actualObj = dailyData.getOpen();
                break;
            case close:
                actualObj = dailyData.getClose();
                break;
            case adjPrice:
                actualObj = dailyData.getAdjPrice();
                break;
            case volumn:
                actualObj = dailyData.getVolumn();
                break;
            case turnover:
                actualObj = dailyData.getTurnover();
                break;
            case pe:
                actualObj = dailyData.getPe();
                break;
            case pb:
                actualObj = dailyData.getPb();
                break;
            case high:
                actualObj = dailyData.getHigh();
                break;
            case low:
                actualObj = dailyData.getLow();
                break;
        }

        return compareObj(actualObj, filter.value, filter.dataType, filter.compareType);
    }

    public static Collection<StockInfoVO> filterStockInfo(Collection<StockInfoVO> source, Collection<Filter> filters)
    {
        LinkedList<StockInfoVO> result = new LinkedList<>();
        Iterator<StockInfoVO> iterator = source.iterator();
        outer:
        while (iterator.hasNext())
        {
            StockInfoVO vo = iterator.next();

            for (Filter filter: filters)
            {
                if(!canSatisfy(vo, filter))
                {
                    continue outer;
                }
            }

            result.add(vo);
        }

        return result;
    }

    public static Collection<DailyDataVO> filterDailyData(Collection<DailyDataVO> source, Collection<Filter> filters)
    {
        LinkedList<DailyDataVO> result = new LinkedList<>();
        Iterator<DailyDataVO> iterator = source.iterator();
        outer:
        while (iterator.hasNext())
        {
            DailyDataVO vo = iterator.next();

            for (Filter filter: filters)
            {
                if(!canSatisfy(vo, filter))
                {
                    continue outer;
                }
            }

            result.add(vo);
        }

        return result;
    }
}
