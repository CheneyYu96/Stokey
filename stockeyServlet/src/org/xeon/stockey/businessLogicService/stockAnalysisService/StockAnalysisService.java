
package org.xeon.stockey.businessLogicService.stockAnalysisService;

import javafx.util.Pair;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.vo.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * 股票统计分析模块的接口
 * Created by Sissel on 2016/4/6.
 */
public interface StockAnalysisService {

    /**
     * 获取指定股票的日K
     * @param stockCode 股票代码
     * @param begin 开始日期，包括
     * @param end 结束日期，包括
     * @return 对应的一系列柱子的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<CandlestickVO> getCandlestickLineD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的周K
     * 如果begin不是正好是周的开始日子，那就从这天开始算到下一个开始日子也算一周
     * 如果end不是正好是周的结束日子，那从前一个结束日子到这天也算一周
     * @param stockCode 股票代码
     * @param begin 开始日期，包括
     * @param end 结束日期，包括
     * @return 对应的一系列柱子的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<CandlestickVO> getCandlestickLineW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的月K
     * 如果begin不是正好是月的开始日子，那就从这天开始算到下一个开始日子也算一月
     * 如果end不是正好是月的结束日子，那从前一个结束日子到这天也算一月
     * @param stockCode 股票代码
     * @param begin 开始日期，包括
     * @param end 结束日期，包括
     * @return 对应的一系列柱子的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<CandlestickVO> getCandlestickLineM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的日MACD
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<MacdVO> getMacdD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的周MACD（对应后缀W）
     * 如果begin不是正好是周的开始日子，那就从这天开始算到下一个开始日子也算一周
     * 如果end不是正好是周的结束日子，那从前一个结束日子到这天也算一周
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<MacdVO> getMacdW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的月MACD（对应后缀W）
     * 如果begin不是正好是月的开始日子，那就从这天开始算到下一个开始日子也算一月
     * 如果end不是正好是月的结束日子，那从前一个结束日子到这天也算一月
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<MacdVO> getMacdM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的日RSI
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<RsiVO> getRsiD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的周RSI
     * 如果begin不是正好是周的开始日子，那就从这天开始算到下一个开始日子也算一周
     * 如果end不是正好是周的结束日子，那从前一个结束日子到这天也算一周
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<RsiVO> getRsiW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的月RSI
     * 如果begin不是正好是月的开始日子，那就从这天开始算到下一个开始日子也算一月
     * 如果end不是正好是月的结束日子，那从前一个结束日子到这天也算一月
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<RsiVO> getRsiM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;
    
    /**
     * 获取指定股票的日KDJ
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<KdjVO> getKdjD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的周KDJ
     * 如果begin不是正好是周的开始日子，那就从这天开始算到下一个开始日子也算一周
     * 如果end不是正好是周的结束日子，那从前一个结束日子到这天也算一周
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<KdjVO> getKdjW(String stockCode, LocalDate begin, LocalDate end)throws NetworkConnectionException;

    /**
     * 获取指定股票的月KDJ
     * 如果begin不是正好是月的开始日子，那就从这天开始算到下一个开始日子也算一月
     * 如果end不是正好是月的结束日子，那从前一个结束日子到这天也算一月
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<KdjVO> getKdjM(String stockCode, LocalDate begin, LocalDate end)throws NetworkConnectionException;

    /**
     * 获取指定股票的日涨跌幅
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<ChangeVO> getChangeD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的周涨跌幅
     * 如果begin不是正好是周的开始日子，那就从这天开始算到下一个开始日子也算一周
     * 如果end不是正好是周的结束日子，那从前一个结束日子到这天也算一周
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<ChangeVO> getChangeW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的月涨跌幅
     * 如果begin不是正好是月的开始日子，那就从这天开始算到下一个开始日子也算一月
     * 如果end不是正好是月的结束日子，那从前一个结束日子到这天也算一月
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<ChangeVO> getChangeM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的日成交量
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<VolumeVO> getVolumeD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的周成交量
     * 如果begin不是正好是周的开始日子，那就从这天开始算到下一个开始日子也算一周
     * 如果end不是正好是周的结束日子，那从前一个结束日子到这天也算一周
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<VolumeVO> getVolumeW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的月成交量
     * 如果begin不是正好是月的开始日子，那就从这天开始算到下一个开始日子也算一月
     * 如果end不是正好是月的结束日子，那从前一个结束日子到这天也算一月
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<VolumeVO> getVolumeM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的日换手率
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<PointVO> getTurnoverD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的周换手率
     * 如果begin不是正好是周的开始日子，那就从这天开始算到下一个开始日子也算一周
     * 如果end不是正好是周的结束日子，那从前一个结束日子到这天也算一周
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<PointVO> getTurnoverW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的月换手率
     * 如果begin不是正好是月的开始日子，那就从这天开始算到下一个开始日子也算一月
     * 如果end不是正好是月的结束日子，那从前一个结束日子到这天也算一月
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的一系列点的VO的集合
     * @throws NetworkConnectionException 网络异常
     */
    Collection<PointVO> getTurnoverM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的日涨跌幅方差
     * @param stockCode 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的方差值
     * @throws NetworkConnectionException 网络异常
     */
    double getVarianceOfChangeD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定2只股票的日涨跌幅协方差
     * @param stockCode1 某个股票代码
     * @param stockCode2 另一个股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的协方差值
     * @throws NetworkConnectionException 网络异常
     */
    double getCovarianceOfChangeD(String stockCode1, String stockCode2, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定2只股票的日涨跌幅相关系数
     * @param stockCode1 某个股票代码
     * @param stockCode2 另一个股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的相关系数
     * @throws NetworkConnectionException 网络异常
     */
    double getCorrelationCoefficientD(String stockCode1, String stockCode2, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的影响的饼图
     * ！！！目前没有股数信息，所以，直接统一用1000股来算
     *
     * @param stockCodes 股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的影响的饼图
     * @throws NetworkConnectionException 网络异常
     */
    PieVO getInfluencePie(Collection<String> stockCodes, LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的影响的饼图
     * ！！！目前没有股数信息，所以，直接统一用1000股来算
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的影响的饼图
     * @throws NetworkConnectionException 网络异常
     */
    PieVO getIndustryInfluencePie(LocalDate begin, LocalDate end) throws NetworkConnectionException;

    /**
     * 获取指定股票的日走势
     * @param stockCode 某个股票代码
     * @param begin 开始日期
     * @param end 结束日期
     * @return 对应的走势的点
     * @throws NetworkConnectionException 网络异常
     */
    Collection<PointVO> getTrendD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException;
}
