/**
 * @author nians
 * @2016年4月10日
 */
package org.xeon.stockey.vo;

import org.xeon.stockey.util.WithLocalDate;

import java.time.LocalDate;

/**
 * @author nians
 * 
 */
public class KdjVO implements WithLocalDate
{
	public double kValue;
	public double dValue;
	public double jValue;
	public LocalDate date;
	
	/**
	 * 
	 * @param kValue K值
	 * @param dValue D值
	 * @param jValue J值
	 * @param date x轴上的日期
	 */
	public KdjVO(){}
	public KdjVO(double kValue, double dValue, double jValue, LocalDate date) {
		this.kValue = kValue;
		this.dValue = dValue;
		this.jValue = jValue;
		this.date = date;
	}
	
	@Override
    public String toString(){
		return (date==null)?("date:"+date.toString()+" "):""
			+"K:"+this.kValue+" D:"+this.dValue+" J:"+this.jValue;
    }

	@Override
	public LocalDate getDate()
	{
		return date;
	}
}
