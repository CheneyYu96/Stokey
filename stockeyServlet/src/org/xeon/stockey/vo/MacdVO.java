package org.xeon.stockey.vo;

import org.xeon.stockey.util.WithLocalDate;

import java.time.LocalDate;

/**
 * 一个MACD点
 * Created by Sissel on 2016/4/11.
 */
public class MacdVO implements WithLocalDate
{
	public double dif; // 离差值
	public double dem; // 讯号线
	public double osc; // 柱形图
	public LocalDate date; // 日期

	public MacdVO(LocalDate date) {
		this.date = date;
	}

	public MacdVO(LocalDate date, double dif, double dem, double osc) {
		this.date = date;
		this.dif = dif;
		this.dem = dem;
		this.osc = osc;
	}

	public double getDif() {
		return dif;
	}

	public void setDif(double dif) {
		this.dif = dif;
	}

	public double getDem() {
		return dem;
	}

	public void setDem(double dem) {
		this.dem = dem;
	}

	public double getOsc() {
		return osc;
	}

	public void setOsc(double osc) {
		this.osc = osc;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
