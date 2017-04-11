package org.xeon.stockey.vo;

import org.xeon.stockey.businessLogic.stockAccess.FileBenchmarkSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAnalysis.StockAnalysisImpl;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.businessLogicService.stockAnalysisService.StockAnalysisService;
import org.xeon.stockey.util.CollectionHelper;

import java.time.LocalDate;
import java.util.Collection;

/**
 * 对比列表中的每一行
 * Created by Sissel on 2016/4/13.
 */
public class ContrastVO
{
    public StockInfoVO stockInfoVO;
    public double pe;
    public double pb;
    public double changeWeek1;
    public double varianceWeek1;
    public double changeMonth1;
    public double varianceMonth1;
    public double volumeWeek1;

    public ContrastVO(StockInfoVO stockInfoVO, double pe, double pb, double changeWeek1,
                      double varianceWeek1, double changeMonth1, double varianceMonth1, double volumeWeek1)
    {
        this.stockInfoVO = stockInfoVO;
        this.pe = pe;
        this.pb = pb;
        this.changeWeek1 = changeWeek1;
        this.varianceWeek1 = varianceWeek1;
        this.changeMonth1 = changeMonth1;
        this.varianceMonth1 = varianceMonth1;
        this.volumeWeek1 = volumeWeek1;
    }

    public ContrastVO(StockInfoVO stockInfoVO) throws NetworkConnectionException
    {
        StockAnalysisService stockAnalysisService = new StockAnalysisImpl();
        StockSourceService stockSourceService = new FileStockSourceImpl();
        StockSourceService benchmarkSourceService = new FileBenchmarkSourceImpl();
        String stockCode = stockInfoVO.getStockCode();

        this.stockInfoVO = stockInfoVO;

        DailyDataVO dailyDataVO;

        // TODO: 2016/4/13 hard code
        if (stockCode.startsWith("bm"))
        {
            dailyDataVO = benchmarkSourceService.getLatestDailyData(stockCode);
        }
        else
        {
            dailyDataVO = stockSourceService.getLatestDailyData(stockCode);
        }
        pe = dailyDataVO.getPe();
        pb = dailyDataVO.getPb();

        LocalDate nowDate = LocalDate.now();

        Collection<ChangeVO> changeWeekVOs = stockAnalysisService.getChangeW(stockCode, nowDate.minusWeeks(3), nowDate);
        changeWeek1 = CollectionHelper.getLastOne(changeWeekVOs).change;

        Collection<ChangeVO> changeMonthVOs = stockAnalysisService.getChangeM(stockCode, nowDate.minusMonths(3), nowDate);
        changeMonth1 = CollectionHelper.getLastOne(changeMonthVOs).change;

        varianceWeek1 = stockAnalysisService.getVarianceOfChangeD(stockCode, nowDate.minusWeeks(1), nowDate);
        varianceMonth1 = stockAnalysisService.getVarianceOfChangeD(stockCode, nowDate.minusMonths(1), nowDate);

        Collection<VolumeVO> volumeWeekVOs = stockAnalysisService.getVolumeW(stockCode, nowDate.minusWeeks(3), nowDate);
        volumeWeek1 = CollectionHelper.getLastOne(volumeWeekVOs).volume;

        System.out.println("finish build contrastVO");
    }

    public String getCodeAndName()
    {
        return stockInfoVO.getStockCode() + stockInfoVO.getName();
    }

}
