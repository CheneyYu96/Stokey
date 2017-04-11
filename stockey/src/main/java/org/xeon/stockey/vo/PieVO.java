package org.xeon.stockey.vo;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个饼图的结构
 * Created by Sissel on 2016/4/13.
 */
public class PieVO
{
    public String name;
    public LocalDate begin;
    public LocalDate end;

    // key是股票代码， value是对应的股票信息
    public Map<String, StockInfoVO> stockMap = new HashMap<>();

    // key是股票代码， value是份额
    public Map<String, Double> shareMap = new HashMap<>();

    public PieVO(String name, LocalDate begin, LocalDate end)
    {
        this.begin = begin;
        this.name = name;
        this.end = end;
    }

    public void putStock(StockInfoVO stockInfoVO, double share)
    {
        String code = stockInfoVO.getStockCode();
        stockMap.put(code, stockInfoVO);
        shareMap.put(code, share);
    }

}
