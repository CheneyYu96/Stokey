package org.xeon.stockey.businessLogic.stockAnalysis;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xeon.stockey.businessLogic.stockAccess.DBStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockAccessController;
import org.xeon.stockey.businessLogic.stockAccess.StockInfoFilterImpl;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockInfoFilterService;
import org.xeon.stockey.po.StockInfoPO;
import org.xeon.stockey.vo.DailyDataVO;
import org.xeon.stockey.vo.StockInfoVO;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by nians on 2016/6/19.
 */

@RestController
@RequestMapping("/stock/recommend")
public class StockRecommendation {
    @RequestMapping("/oneStock")
    public StockInfoVO recommendStock(String stockCode) throws NetworkConnectionException {
        StockAccessController controller = new StockAccessController();
        DBStockSourceImpl impl = new DBStockSourceImpl();
        Iterator<StockInfoVO> it = controller.searchStock(stockCode).iterator();
        StockInfoVO vo = it.next();
        DailyDataVO daily = vo.getLatestDailyDataVO();

        StockInfoFilterService filterService = new StockInfoFilterImpl(impl);
        Iterator<StockInfoVO> resultIt = filterService.filterIndustry(vo.getType()).getResult().iterator();

        return resultIt.next();


    }

    @RequestMapping("/moreStock")
    public Collection<StockInfoVO> recommendMoreStock(
            @RequestParam("stockCodes") Collection<String> stockCodes) throws NetworkConnectionException {
        StockAccessController controller = new StockAccessController();
        DBStockSourceImpl impl = new DBStockSourceImpl();
        Collection<StockInfoVO> result = new ArrayList<>();
        for(String stockCode : stockCodes) {
            Iterator<StockInfoVO> it = controller.searchStock(stockCode).iterator();
            if(it.hasNext()){
            	StockInfoVO vo = it.next();
            	DailyDataVO daily = vo.getLatestDailyDataVO();
            	 StockInfoFilterService filterService = new StockInfoFilterImpl(impl);
                 Iterator<StockInfoVO> resultIt = filterService.filterIndustry(vo.getType()).getResult().iterator();

                 for (int i = 0; i < 3 && resultIt.hasNext(); i++) {
                     result.add(resultIt.next());
                 }
            }
        }
        return result;
    }
}
