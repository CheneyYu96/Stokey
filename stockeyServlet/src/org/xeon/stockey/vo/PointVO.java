package org.xeon.stockey.vo;

import org.xeon.stockey.util.WithLocalDate;

import java.time.LocalDate;

/**
 * 表示图表上的一个点
 * Created by Sissel on 2016/4/6.
 */
public class PointVO implements WithLocalDate
{
    public double y;
    public LocalDate date;

    /**
     * @param y y轴上的数值
     * @param date x轴上的时间
     */
    public PointVO(double y, LocalDate date)
    {
        this.y = y;
        this.date = date;
    }

    @Override
    public LocalDate getDate()
    {
        return date;
    }
}
