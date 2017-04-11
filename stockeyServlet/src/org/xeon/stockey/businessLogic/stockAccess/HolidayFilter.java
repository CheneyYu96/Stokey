package org.xeon.stockey.businessLogic.stockAccess;

import org.xeon.stockey.po.DailyDataPO;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 用来从数据到逻辑过程中过滤掉本来不应该存在的假日的数据
 * 假设：星期一到星期五的数据一定有，本来不开盘的节假日的数据如果给了，那和前一天开盘的数据一mol一样
 * Created by Sissel on 2016/4/16.
 */
class HolidayFilter
{
    private static boolean isSame(DailyDataPO po1, DailyDataPO po2)
    {
        return po1.getAdjPrice() == po2.getAdjPrice()
                && po1.getClose() == po2.getClose()
                && po1.getOpen() == po2.getOpen()
                && po1.getHigh() == po2.getHigh()
                && po1.getLow() == po2.getLow()
                && po1.getTurnover() == po2.getTurnover()
                && po1.getVolumn() == po2.getVolumn();
    }

    public static LinkedList<DailyDataPO> filterFromIterator(Iterator<DailyDataPO> poIterator)
    {
        LinkedList<DailyDataPO> result = new LinkedList<>();

        if (!poIterator.hasNext()) // defensive code
        {
            return result;
        }

        DailyDataPO before = poIterator.next();
        result.add(before);
        while (poIterator.hasNext())
        {
            DailyDataPO temp = poIterator.next();
            if (!isSame(before, temp)) // add
            {
                result.add(temp);
                before = temp;
            }
        }

        return result;
    }
}
