package org.xeon.stockey.data.impl;

import javafx.util.Pair;
import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.data.dao.DailyDataDAO;
import org.xeon.stockey.po.DailyDataPO;

import java.util.*;

/**
 * Created by nians on 2016/6/18.
 */
public class DataWarehouse {
    private static boolean initialized = false;
    private static final int MAX_SIZE = 5;
    private static Queue<Pair<String, Map<String, DailyDataPO>>> stockQueue;
    private static DailyDataDAO dao;
    private static String lastAttempt;

    private DataWarehouse() {
    }

    public static void init(DailyDataDAO dao) {
        DataWarehouse.dao = dao;
        initialized = true;
        stockQueue = new ArrayDeque<>();
        addStock("000300");
        addStock("600606");
    }

    public static Iterator<DailyDataPO> attempt(String stockCode, Calendar startDate, Calendar endDate) {
        Iterator<DailyDataPO> it = DataWarehouse.getData(stockCode, startDate, endDate);
        if (it != null) return it;
        if ((it==null&&stockQueue.size()<5)||(stockCode.equals(lastAttempt) && it == null)) {
            DataWarehouse.addStock(stockCode);
            it = DataWarehouse.getData(stockCode, startDate, endDate);
            return it;
        }
        lastAttempt = stockCode;
        return null;
    }

    public static Iterator<DailyDataPO> getData(String stockCode, Calendar startDate, Calendar endDate) {
        ArrayList<DailyDataPO> result = new ArrayList<>();
        Map<String, DailyDataPO> map = null;
        for (Pair<String, Map<String, DailyDataPO>> p : stockQueue) {
            if (p.getKey().equals(stockCode)) {
                map = p.getValue();
            }
        }
        if (map == null) return null;

        if (stockCode.length() != 6) {
            stockCode = stockCode.substring(2);
        }

        do {
            String id = stockCode + UtilityTools.Cal2String(startDate).replace("-", "");
            DailyDataPO po = map.get(id);
            if (po != null)
                result.add(po);
            startDate.add(Calendar.DATE, 1);
        } while (!startDate.equals(endDate));

        return result.iterator();
    }

    private static void addStock(String stockCode) {
        if (stockQueue.size() == 5) {
            stockQueue.poll();
        }
        Map<String, DailyDataPO> map = new HashMap<>();
        Iterator<DailyDataPO> it = dao.findDailyData(stockCode, UtilityTools.String2Cal("2012-01-02"), UtilityTools.String2Cal("2016-06-10"));
        while (it.hasNext()) {
            DailyDataPO po = it.next();
            map.put(po.getId(), po);
        }
        Pair<String, Map<String, DailyDataPO>> pair = new Pair<>(stockCode, map);
        stockQueue.add(pair);
    }

}
