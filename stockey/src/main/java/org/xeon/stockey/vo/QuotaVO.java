package org.xeon.stockey.vo;

/**
 * Created by yuminchen on 16/6/17.
 */
public class QuotaVO {

    public String stockCode;

    public double average;
    public double variance;
    public VectorVO shortTerm;
    public VectorVO midTerm;
    public VectorVO longTerm;
    public VectorVO percentage;

    public QuotaVO(String stockCode, double average,
                   double variance, VectorVO shortTerm, VectorVO midTerm, VectorVO longTerm) {
        this.stockCode = stockCode;
        this.average = average;
        this.variance = variance;
        this.shortTerm = shortTerm;
        this.midTerm = midTerm;
        this.longTerm = longTerm;
        this.percentage = new VectorVO(1,1,1,1,1,1);
    }

    public QuotaVO(String stockCode, double average, double variance,
                   VectorVO shortTerm, VectorVO midTerm, VectorVO longTerm, VectorVO percentage) {
        this(stockCode,average,variance,shortTerm, midTerm, longTerm);
        this.percentage = percentage;
    }

    public void setPercentage(VectorVO percentage){
        this.percentage = percentage;
    }
}
