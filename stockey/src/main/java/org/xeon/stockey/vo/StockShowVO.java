package org.xeon.stockey.vo;

/**
 * Created by yuminchen on 16/5/26.
 */
public class StockShowVO {
    public String stockName;

    public String stockCode;

    public double money;

    public int shareNumber;

    public StockShowVO(int shareNumber, double money, String stockCode, String stockName) {
        this.shareNumber = shareNumber;
        this.money = money;
        this.stockCode = stockCode;
        this.stockName = stockName;
    }

    public StockShowVO() {
    }
}
