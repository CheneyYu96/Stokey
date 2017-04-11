package org.xeon.stockey.vo;

import org.xeon.stockey.util.WithLocalDate;

import java.time.LocalDate;

/**
 * 表示K线图的一个柱子
 * Created by Sissel on 2016/4/6.
 */
public class CandlestickVO implements WithLocalDate
{
    public double open;
    public double close;
    public double high;
    public double low;
    public LocalDate date;
    public double ma5 = Double.NaN;
    public double ma10 = Double.NaN;
    public double ma20 = Double.NaN;
    public double ma30 = Double.NaN;

    /**
     * @param open 开盘价
     * @param close 收盘价
     * @param high 最高价
     * @param low 最低价
     * @param date x轴上的日期
     */
    public CandlestickVO(double open, double close, double high, double low, LocalDate date)
    {
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.date = date;
    }

    @Override
    public String toString()
    {
        if(date == null)
        {
            return " open:" + open + " close:" + close + " high:" + high + " low:" + low;
        }
        else
        {
            return "date:" + date.toString()
                    + " open:" + open + " close:" + close + " high:" + high + " low:" + low;
        }
    }

    @Override
    public LocalDate getDate()
    {
        return date;
    }

    public double getMa30(){return ma30;}
}
