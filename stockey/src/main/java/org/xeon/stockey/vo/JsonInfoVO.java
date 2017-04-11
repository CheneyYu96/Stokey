package org.xeon.stockey.vo;

import java.time.LocalDate;

/**
 * Created by yuminchen on 16/6/1.
 */
public class JsonInfoVO {

    private LocalDate date;

    private double totalMoney;

    public LocalDate getDate() {
        return date;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }
}
