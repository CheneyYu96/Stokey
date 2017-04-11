package org.xeon.stockey.vo;

import org.xeon.stockey.util.WithLocalDate;

import java.time.LocalDate;

/**
 * 包含6日，12日，24日的RSI
 * Created by Sissel on 2016/4/11.
 */
public class RsiVO implements WithLocalDate
{
    public LocalDate date;
    public double rsi6;
    public double rsi12;
    public double rsi24;

    public RsiVO(LocalDate date)
    {
        this.date = date;
    }

    public RsiVO(LocalDate date, double rsi6, double rsi12, double rsi24)
    {
        this.date = date;
        this.rsi6 = rsi6;
        this.rsi12 = rsi12;
        this.rsi24 = rsi24;
    }

    @Override
    public LocalDate getDate()
    {
        return date;
    }
}
