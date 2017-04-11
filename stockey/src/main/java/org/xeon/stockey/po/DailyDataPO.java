package org.xeon.stockey.po;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 用于记录一支股票一天的数据
 * @author nians
 *
 */
public class DailyDataPO{
	/**
	 * 
	 */
	//用于存储的id，由8位股票代码+yyyyMMdd构成，一共14位
	private String id;
	private Calendar theDate;
	private double close;
	private double high;
	private double low;
	private double open;
	private double volumn;
	private double turnover;
	private double adjPrice;
	private double pe;
	private double pb;
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
	public DailyDataPO(){}
	public DailyDataPO(String symbol, Calendar theDate, double close, double high, double low, double open, double volumn, double turnover,
			double adjPrice, double pe, double pb) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		this.id = symbol+ sdf.format(theDate.getTime());
		this.theDate = theDate;
		this.close = close;
		this.high = high;
		this.low = low;
		this.open = open;
		this.volumn = volumn;
		this.turnover = turnover;
		this.adjPrice = adjPrice;
		this.pe = pe;
		this.pb = pb;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Calendar getTheDate() {
		return theDate;
	}
	public void setTheDate(Calendar theDate) {
		this.theDate = theDate;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
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
	public void print(){
		System.out.println(this.getAdjPrice()+" "+this.getClose()+" "+
				this.getHigh()+" "+this.getId()+" "+this.getLow()+" "
				+this.getOpen()+" "+this.getPb()+" "+this.getPe()
				+" "+this.getTurnover()+" "+this.getVolumn());
	}
}
