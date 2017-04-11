package org.xeon.stockey.businessLogicService.stockAccessService;

import java.time.LocalDate;
import java.util.Collection;

import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.SetOperable;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * Created by Sissel on 2016/3/5.
 * 提供根据条件过滤DailyDataVO的服务
 * lazy, 在调用getResult方法之前不实际查询数据
 */
public interface StockDailyDataFilterService
{
    /**
     * 获取符合条件的DailyDataVO的集合
     * @return 符合条件的DailyDataVO的集合
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<DailyDataVO> getResult() throws NetworkConnectionException;

    /**
     * 指定对应的StockInfoVO
     * @param stockInfoVO 要指定的StockInfoVO
     * @return 指定好StockInfoVO的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService setStockInfoVO(StockInfoVO stockInfoVO);

    /**
     * 获取该对象指定的StockInfoVO
     * @return 该对象指定的StockInfoVO
     */
    public StockInfoVO getStockInfoVO();

    /**
     * 指定stock数据的来源
     * @param stockSource 指定的stock数据源
     * @return 指定了该数据源的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService setStockSource(StockSourceService stockSource);

    /**
     * 获取该对象指定的数据源
     * @return 该对象的数据源
     */
    public StockSourceService getStockSource();

    /**
     * 增加筛选条件，符合时间取值范围（两边闭区间）
     * @param begin 开始时间
     * @param end 结束时间
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterDate(LocalDate begin, LocalDate end);

    /**
     * 增加筛选条件，符合 开盘价 取值范围（两边闭区间）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterOpen(double lowerBound, double upperBound);

    /**
     * 增加筛选条件，符合 最高价 取值范围（两边闭区间）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterHigh(double lowerBound, double upperBound);

    /**
     * 增加筛选条件，符合 最低价 取值范围（两边闭区间）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterLow(double lowerBound, double upperBound);

    /**
     * 增加筛选条件，符合  收盘价 取值范围（两边闭区间）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterClose(double lowerBound, double upperBound);

    /**
     * 增加筛选条件，符合 后复权价 取值范围（两边闭区间）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterAdj_Price(double lowerBound, double upperBound);

    /**
     * 增加筛选条件，符合 成交量 取值范围（两边闭区间）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterVolume(double lowerBound, double upperBound);

    /**
     * 增加筛选条件，符合 换手率 取值范围（两边闭区间）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterTurnover(double lowerBound, double upperBound);

    /**
     * 增加筛选条件，符合 市盈率 取值范围（两边闭区间）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterPE(double lowerBound, double upperBound);

    /**
     * 增加筛选条件，符合 市净率 取值范围（两边闭区间）
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService filterPB(double lowerBound, double upperBound);

    /**
     * 增加筛选条件
     * @param filter 要增加的筛选条件
     * @return 增加此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService addFilter(Filter filter);

    /**
     * 去除筛选条件（根据equals方法判断）
     * @param filter 要去除的筛选条件
     * @return 去除此筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService removeFilter(Filter filter);

    /**
     * 去除所有筛选条件
     * @return 增加所有筛选条件的StockDailyDataFilterService对象
     */
    public StockDailyDataFilterService clearFilters();
}
