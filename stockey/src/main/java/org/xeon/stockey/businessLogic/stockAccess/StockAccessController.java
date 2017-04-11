package org.xeon.stockey.businessLogic.stockAccess;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockInfoFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.vo.StockInfoVO;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;

@RestController
@RequestMapping("/access/stock")
public class StockAccessController {

    private StockSourceService sourceService = new DBStockSourceImpl();
    private StockInfoFilterService stockInfoFilterService = new StockInfoFilterImpl(sourceService);

    public StockAccessController() {

    }

    @RequestMapping("/search")
    public Collection<StockInfoVO> searchStock(
            @RequestParam("stockCode") String stockCode) throws NetworkConnectionException {
        stockInfoFilterService.clearFilters();
        System.err.println("start @" + Calendar.getInstance().getTimeInMillis());
        if (!Character.isLetter(stockCode.charAt(0))&&!Character.isDigit(stockCode.charAt(0)))
            return stockInfoFilterService.filterName(stockCode).getResult();
        else
            return stockInfoFilterService.filterStockCode(stockCode).getResult();
    }

    public static void main(String[] args) {
        System.err.println(Calendar.getInstance().getTimeInMillis());
    }
}
