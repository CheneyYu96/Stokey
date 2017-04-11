package org.xeon.stockey.businessLogicService.stockAccessService;

import java.util.Collection;

import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * Created by Sissel on 2016/3/5.
 * 提供StockInfoVO和DailyDataVO的底层提供，支持Filter
 */
public interface StockSourceService
{
    /**
     * 获取所有StockInfo
     * @return 所有StockInfo的Collection
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<StockInfoVO> getAllStockInfo() throws NetworkConnectionException;

    /**
     * 找出符合所有条件的所有的StockInfo
     * @param filters 所有过滤条件Filter的Collection
     * @return 符合所有条件的所有的StockInfo的Collection
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<StockInfoVO> filterStockInfo(Collection<Filter> filters) throws NetworkConnectionException;

    /**
     * 找出符合所有条件的所有的StockInfo
     * @param filters 所有过滤条件Filter的Collection
     * @return 符合所有条件的所有的StockInfo的Collection
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<StockInfoVO> filterStockInfo(Filter...filters) throws NetworkConnectionException;

    /**
     * 找出符合所有条件的所有的StockInfo
     * @param stockInfoFilterService 包含所有过滤条件Filter的StockInfoFilterService对象
     * @return 符合所有条件的所有的StockInfo的Collection
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<StockInfoVO> filterStockInfo(StockInfoFilterService stockInfoFilterService) throws NetworkConnectionException;

    /**
     * 某只股票的StockInfoVO
     * @param stockCode 想要获取的股票的股票代码
     * @return 对应股票的StockInfoVO
     * @throws NetworkConnectionException 网络异常
     */
    public StockInfoVO getStockInfo(String stockCode) throws NetworkConnectionException;

    /**
     * 将某股票的所有DailyDataVO返回
     * @param stockCode 想要获取的股票信息的股票的股票代码
     * @return 股票的所有DailyDataVO的Collection
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<DailyDataVO> getAllStockDailyData(String stockCode) throws NetworkConnectionException;

    /**
     * 将某股票的所有DailyDataVO返回
     * @param stockInfoVO 想要获取的股票信息的股票的StockInfoVO
     * @return 股票的所有DailyDataVO的Collection
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<DailyDataVO> getAllStockDailyData(StockInfoVO stockInfoVO) throws NetworkConnectionException;

    /**
     * 获取某股票的符合筛选条件的DailyDataVO
     * @param stockInfoVO 想要获取的股票信息的股票的StockInfoVO
     * @param filters 筛选条件Filter的Collection
     * @return 某股票的符合筛选条件的DailyDataVO的Collection
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<DailyDataVO> filterStockDailyData(StockInfoVO stockInfoVO, Collection<Filter> filters) throws NetworkConnectionException;

    /**
     * 获取某股票的符合筛选条件的DailyDataVO
     * @param stockInfoVO 想要获取的股票信息的股票的StockInfoVO
     * @param filters 筛选条件Filter的Collection
     * @return 某股票的符合筛选条件的DailyDataVO的Collection
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<DailyDataVO> filterStockDailyData(StockInfoVO stockInfoVO, Filter...filters) throws NetworkConnectionException;

    /**
     * 获取某股票的符合筛选条件的DailyDataVO
     * @param stockDailyDataFilterService 包含StockInfoVO和所有过滤条件的StockDailyDataFilterService
     * @return 某股票的符合筛选条件的DailyDataVO的Collection
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<DailyDataVO> filterStockDailyData(StockDailyDataFilterService stockDailyDataFilterService) throws NetworkConnectionException;

    /**
     * 得到一个股票的最近一天的数据
     * @param stockCode 股票代码
     * @return 股票的最近一天的数据
     * @throws NetworkConnectionException 网络异常
     */
    public DailyDataVO getLatestDailyData(String stockCode) throws NetworkConnectionException;
}
