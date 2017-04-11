package org.xeon.stockey.vo;

import org.xeon.stockey.util.WithLocalDate;

import java.time.LocalDate;

/**
 * 涨跌幅的点
 * Created by Sissel on 2016/4/12.
 */
public class ChangeVO implements WithLocalDate
{
    public double change;
    public LocalDate date;

    public ChangeVO(LocalDate date)
    {
        this.date = date;
    }

    public ChangeVO(double change, LocalDate date)
    {
        this.change = change;
        this.date = date;
    }

    @Override
    public LocalDate getDate()
    {
        return date;
    }
}
