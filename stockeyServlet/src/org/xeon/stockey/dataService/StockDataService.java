package org.xeon.stockey.dataService;

import java.util.Calendar;
import java.util.Iterator;

import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.ResultMessage;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StockInfoPO;

/**
 * Created by nians on 2016/3/6.
 * 对BusinessLogic层提供Stock数据访问
 */
public interface StockDataService {
//	/**
//	 * 按照某种标准查找股票
//	 * @param criteria 查找标准，key为查找属性，value为查找内容
//	 * @return 结果可能为多个，用Iterator遍历
//	 */
//	public Iterator<StockInfoPO> searchStock(Iterator<Filter> criteria);
	
	/**
	 * 获取大盘数据
	 * @return 大盘数据的Iterator
     * @throws NetworkConnectionException 网络错误
	 */
	public Iterator<StockInfoPO> getBenchmark() throws NetworkConnectionException;
	
	/**
	 * 获取所有股票PO
	 * @return 股票Iterator
     * @throws NetworkConnectionException 网络错误
	 */
	public Iterator<StockInfoPO> getAllStock() throws NetworkConnectionException;

	/**
	 * 批量添加股票
	 * @param stocks 要添加的股票
	 * @return ResultMessage，若失败则ResultMessage.msg含有错误信息
     * @throws NetworkConnectionException 网络错误
	 */
	public ResultMessage addStockBat(Iterator<StockInfoPO> stocks) throws NetworkConnectionException;
	
	/**
	 * 获取指定股票的一个时间段的每日数据
	 * @param symbol 股票代码
	 * @param startDate 开始日期 （若为null则默认为最近一个月）
	 * @param endDate 结束日期（若为null则默认为最近一个月）
	 * @return 开始日期到结束日期内的该股票的所有DailyData
	 * @throws NetworkConnectionException 网络错误
	 */
	public Iterator<DailyDataPO> getStockDaily(String symbol, Calendar startDate, Calendar endDate) throws NetworkConnectionException;
}
