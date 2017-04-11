package org.xeon.stockey.businessLogic.utility;

import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StockInfoPO;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

import java.util.*;

/**
 * Created by Sissel on 2016/3/10.
 */
public class SourceDataHelper
{
    // help trans Iterator<DailyDataPO> to Collection<DailyDataVO>
    public static Collection<DailyDataVO> transDailyResult(Iterator<DailyDataPO> iterator)
    {
        List<DailyDataVO> ls = new LinkedList<>();
        while (iterator.hasNext())
        {
            ls.add(new DailyDataVO(iterator.next()));
        }
        return ls;
    }

    // help trans Iterator<StockInfoPO> to Collection<StockInfoVO>
    public static Collection<StockInfoVO> transStockResult(Iterator<StockInfoPO> iterator) throws NetworkConnectionException
    {
        List<StockInfoVO> ls = new LinkedList<>();
        while (iterator.hasNext())
        {
            ls.add(new StockInfoVO(iterator.next()));
        }
        System.err.println("before return ls @" + Calendar.getInstance().getTimeInMillis());
        return ls;
    }
}
