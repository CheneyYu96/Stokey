package org.xeon.stockey.businessLogicService.stockAccessService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.SetOperable;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.vo.StockInfoVO;

/**
 * Created by Sissel on 2016/3/5.
 * 提供根据条件过滤StockInfoVO的服务
 * lazy, 在调用getResult方法之前不实际查询数据
 */
public interface StockInfoFilterService
{
    /**
     * 获取符合条件的StockInfoVO的集合
     * @return 符合条件的StockInfoVO的集合
     * @throws NetworkConnectionException 网络异常
     */
    public Collection<StockInfoVO> getResult() throws NetworkConnectionException;

    /**
     * 指定stock数据的来源
     * @param stockSource 指定的stock数据源
     * @return 指定了该数据源的StockInfoFilterService对象
     */
    public StockInfoFilterService setStockSource(StockSourceService stockSource);

    /**
     * 获取该对象指定的数据源
     * @return 该对象的数据源
     */
    public StockSourceService getStockSource();

    /**
     * 增加筛选条件，符合股票代码
     * @param stockCode 要符合的股票代码
     * @return 增加此筛选条件的StockInfoFilterService对象
     */
    public StockInfoFilterService filterStockCode(String stockCode);


    /**
     * 增加筛选条件，符合交易所
     * @param exchangeEnum 要符合的交易所
     * @return 增加此筛选条件的StockInfoFilterService对象
     */
    public StockInfoFilterService filterExchange(ExchangeEnum exchangeEnum);

    StockInfoFilterService filterIndustry(String industry);

    StockInfoFilterService filterName(String name);

    /**
     * 增加筛选条件
     * @param filter 要增加的筛选条件
     * @return 增加此筛选条件的StockInfoFilterService对象
     */
    public StockInfoFilterService addFilter(Filter filter);

    /**
     * 去除筛选条件（根据equals方法判断）
     * @param filter 要去除的筛选条件
     * @return 去除此筛选条件的StockInfoFilterService对象
     */
    public StockInfoFilterService removeFilter(Filter filter);

    /**
     * 去除所有筛选条件
     * @return 增加所有筛选条件的StockInfoFilterService对象
     */
    public StockInfoFilterService clearFilters();

    /**
     *
     * @return
     */
//    public StockInfoFilterService filterIndustry(String industry);

}
