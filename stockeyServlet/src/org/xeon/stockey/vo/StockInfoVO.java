package org.xeon.stockey.vo;

import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.ExchangeEnum;
import org.xeon.stockey.po.StockInfoPO;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Sissel on 2016/3/8.
 * StockInfoPO的简单转化
 */
public class StockInfoVO implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2306257669009062379L;
	//股票代码
    private String stockCode;
    //股票名称
    private String name;
    //公司简要信息
    private String info;
    //交易市场（沪、深）
    private ExchangeEnum market;
    //所属地域板块
    private String region;
    //所属行业板块
    private String type;
    //最新一天的数据
    private DailyDataVO latestDailyDataVO;

    private StockDataService stockDataService = DataServiceFactory.getStockDataService();

    /**
     *
     * @param stockCode 股票代码
     * @param name 公司简要信息
     * @param info 公司简要信息
     * @param market 交易市场（沪、深）
     * @param region 所属地域板块
     * @param type 所属行业板块
     */
    public StockInfoVO(String stockCode, String name, String info, ExchangeEnum market, String region,
                       String type)
    {
        super();
        this.stockCode = stockCode;
        this.name = name;
        this.info = info;
        this.market = market;
        this.region = region;
        this.type = type;
    }

    public StockInfoVO(StockInfoPO po) throws NetworkConnectionException
    {
        this(po.getSymbol(), po.getName(), po.getInfo(), po.getMarket(), po.getRegion(), po.getType());

        // find the latest daily data
        Iterator<DailyDataPO> iterator = stockDataService.getStockDaily(po.getSymbol(), null, null);
        if(iterator.hasNext())
        {
            DailyDataPO latest = iterator.next();
            while (iterator.hasNext())
            {
                DailyDataPO tmp = iterator.next();
                if(latest.getTheDate().compareTo(tmp.getTheDate()) < 0)
                {
                    latest = tmp;
                }
            }
            latestDailyDataVO = new DailyDataVO(latest);
        }
    }

    public String getStockCode()
    {
        return stockCode;
    }

    public void setStockCode(String code)
    {
        this.stockCode = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public ExchangeEnum getMarket()
    {
        return market;
    }

    public void setMarket(ExchangeEnum market)
    {
        this.market = market;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public DailyDataVO getLatestDailyDataVO()
    {
        return latestDailyDataVO;
    }

    public void setLatestDailyDataVO(DailyDataVO latestDailyDataVO)
    {
        this.latestDailyDataVO = latestDailyDataVO;
    }

    @Override
    public String toString()
    {
        return stockCode + " " + name + " " + market.name() + " " + region + " " + type + " " + info;
    }
}