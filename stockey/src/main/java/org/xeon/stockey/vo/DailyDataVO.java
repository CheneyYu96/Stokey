package org.xeon.stockey.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;

import org.xeon.stockey.businessLogic.utility.DateTypeConverter;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.util.WithLocalDate;

/**
 * Created by Sissel on 2016/3/8.
 * DailyDataPO的简单转化
 */
public class DailyDataVO implements Serializable, WithLocalDate
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 643135400538550332L;
	private String stockCode;
    private LocalDate date;
    private double close;
    private double open;
    private double volumn;
    private double turnover;
    private double adjPrice;
    private double pe;
    private double pb;
    private double high;
	private double low;
    /**
     *
     * @param symbol 注意！这个参数是股票代码
     * @param theDate 数据所属日期
     * @param close 今日收盘价
     * @param open 今日开盘价
     * @param volumn 成交量（手）
     * @param turnover 成交额（万元）
     * @param adjPrice 后复权价
     * @param pe 市盈率
     * @param pb 市净率
     */
    public DailyDataVO(String symbol, Calendar theDate, double close, double open, double volumn, double turnover,
                       double adjPrice, double pe, double pb,double high ,double low)
    {
        this.stockCode = symbol;
        this.date = DateTypeConverter.convert(theDate);
        this.close = close;
        this.open = open;
        this.volumn = volumn;
        this.turnover = turnover;
        this.adjPrice = adjPrice;
        this.pe = pe;
        this.pb = pb;
        this.high = high;
        this.low = low;
    }

    public DailyDataVO(DailyDataPO po)
    {
        this(
                po.getId().substring(0, 6),
                po.getTheDate(),
                po.getClose(),
                po.getOpen(),
                po.getVolumn(),
                po.getTurnover(),
                po.getAdjPrice(),
                po.getPe(),
                po.getPb(),
                po.getHigh(),
                po.getLow()
                );
    }

    @Override
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate theDate) {
        this.date = theDate;
    }
    public double getClose() {
        return close;
    }
    public void setClose(double close) {
        this.close = close;
    }
    public double getOpen() {
        return open;
    }
    public void setOpen(double open) {
        this.open = open;
    }
    public double getVolumn() {
        return volumn;
    }
    public void setVolumn(double volumn) {
        this.volumn = volumn;
    }
    public double getTurnover() {
        return turnover;
    }
    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }
    public double getAdjPrice() {
        return adjPrice;
    }
    public void setAdjPrice(double adjPrice) {
        this.adjPrice = adjPrice;
    }
    public double getPe() {
        return pe;
    }
    public void setPe(double pe) {
        this.pe = pe;
    }
    public double getPb() {
        return pb;
    }
    public void setPb(double pb) {
        this.pb = pb;
    }
    public String getSymbol()
    {
        return stockCode;
    }

    @Override
    public String toString()
    {
        return "id: " + stockCode
                + " date: " + date.toString()
                + " open: " + open
                + " close: " + close
                + " volumn: " + volumn
                + " turnover: " + turnover
                + " adjPrice: " + adjPrice
                + " pe: " + pe
                + " pb: " + pb
                + " high: " + high
                + " low: " + low;
    }

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DailyDataVO that = (DailyDataVO) o;

        if (Double.compare(that.close, close) != 0) return false;
        if (Double.compare(that.open, open) != 0) return false;
        if (Double.compare(that.volumn, volumn) != 0) return false;
        if (Double.compare(that.turnover, turnover) != 0) return false;
        if (Double.compare(that.adjPrice, adjPrice) != 0) return false;
        if (Double.compare(that.pe, pe) != 0) return false;
        if (Double.compare(that.pb, pb) != 0) return false;
        if (Double.compare(that.high, high) != 0) return false;
        if (Double.compare(that.low, low) != 0) return false;
        if (stockCode != null ? !stockCode.equals(that.stockCode) : that.stockCode != null) return false;
        return date != null ? date.equals(that.date) : that.date == null;

    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = stockCode != null ? stockCode.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        temp = Double.doubleToLongBits(close);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(open);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(volumn);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(turnover);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(adjPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(pe);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(pb);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(high);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(low);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
