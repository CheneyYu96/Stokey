package org.xeon.stockey.vo;

import org.xeon.stockey.util.WithLocalDate;

import java.time.LocalDate;

/**
 * 表示成交量的点
 * Created by Sissel on 2016/4/12.
 */
public class VolumeVO implements WithLocalDate
{
    public double volume;
    public LocalDate date;

    public VolumeVO(LocalDate date)
    {
        this.date = date;
    }

    public VolumeVO(double volume, LocalDate date)
    {
        this.volume = volume;
        this.date = date;
    }

    @Override
    public LocalDate getDate()
    {
        return date;
    }
}
