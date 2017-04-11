package org.xeon.stockey.businessLogicService.strategyService;

import net.sf.json.JSONObject;

import java.time.LocalDate;

/**
 * interface to use the strategy module
 * Created by Sissel on 2016/5/28.
 */
public interface StrategyService
{
    /**
     * 运行策略的接口
     * @param pythonCode 用户的python代码
     * @param start 开始时间
     * @param end 结束时间
     * @param capital 起始资金
     * @return JSON格式的结果, 每项的意思见文档
     */
    public JSONObject runStrategy(String pythonCode, LocalDate start, LocalDate end, double capital);
}
