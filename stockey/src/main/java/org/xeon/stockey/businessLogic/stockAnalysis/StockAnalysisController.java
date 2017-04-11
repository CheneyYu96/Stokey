package org.xeon.stockey.businessLogic.stockAnalysis;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAnalysisService.StockAnalysisService;
import org.xeon.stockey.vo.*;

@SpringBootApplication
@RestController
@RequestMapping("/analysis")
public class StockAnalysisController implements StockAnalysisService {
	StockAnalysisImpl impl;

	public StockAnalysisController() {
		impl = new StockAnalysisImpl();
	}

	@Override
	@ResponseBody
	@RequestMapping("/getCandlestickLine/D")
	public Collection<CandlestickVO> getCandlestickLineD(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getCandlestickLineD(stockCode, begin, end);
	}

    @Override
    public Collection<CandlestickVO> getCandlestickLineD(Collection<DailyDataVO> dailyDataVOs, LocalDate begin, LocalDate end) throws NetworkConnectionException {
        return impl.getCandlestickLineD(dailyDataVOs, begin, end);
    }

    @Override
	@ResponseBody
	@RequestMapping("/getCandlestickLine/W")
	public Collection<CandlestickVO> getCandlestickLineW(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getCandlestickLineW(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getCandlestickLine/M")
	public Collection<CandlestickVO> getCandlestickLineM(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getCandlestickLineM(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getMacd/D")
	public Collection<MacdVO> getMacdD(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getMacdD(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getMacd/W")
	public Collection<MacdVO> getMacdW(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getMacdW(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getMacd/M")
	public Collection<MacdVO> getMacdM(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getMacdM(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getRsi/D")
	public Collection<RsiVO> getRsiD(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getRsiD(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getRsi/W")
	public Collection<RsiVO> getRsiW(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getRsiW(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getRsi/M")
	public Collection<RsiVO> getRsiM(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getRsiM(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getKdj/D")
	public Collection<KdjVO> getKdjD(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
        System.err.println("attempt to get kdj for " + stockCode);
        return impl.getKdjD(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getKdj/W")
	public Collection<KdjVO> getKdjW(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getKdjW(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getKdj/M")
	public Collection<KdjVO> getKdjM(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getKdjM(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getChange/D")
	public Collection<ChangeVO> getChangeD(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getChangeD(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getChange/W")
	public Collection<ChangeVO> getChangeW(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getChangeW(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getChange/M")
	public Collection<ChangeVO> getChangeM(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getChangeM(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getVolume/D")
	public Collection<VolumeVO> getVolumeD(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getVolumeD(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getVolume/W")
	public Collection<VolumeVO> getVolumeW(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getVolumeW(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getVolume/M")
	public Collection<VolumeVO> getVolumeM(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getVolumeM(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getTurnover/D")
	public Collection<PointVO> getTurnoverD(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getTurnoverD(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getTurnover/W")
	public Collection<PointVO> getTurnoverW(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getTurnoverW(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getTurnover/M")
	public Collection<PointVO> getTurnoverM(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getTurnoverM(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getVarianceOfChange/D")
	public double getVarianceOfChangeD(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getVarianceOfChangeD(stockCode, begin, end);
	}
    @Override
    public double getVarianceOfChangeD(Collection<DailyDataVO> dailyVOs, LocalDate begin, LocalDate end) throws NetworkConnectionException {
        return impl.getVarianceOfChangeD(dailyVOs, begin, end);
    }
	@ResponseBody
	@RequestMapping("/getCovarianceOfChange/D")
	public double getCovarianceOfChangeD(
			@RequestParam("stockCode1") String stockCode1,
			@RequestParam("stockCode2") String stockCode2,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getCovarianceOfChangeD(stockCode1, stockCode2, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getCorrelationCoefficient/D")
	public double getCorrelationCoefficientD(
			@RequestParam("stockCode1") String stockCode1,
			@RequestParam("stockCode2") String stockCode2,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getCorrelationCoefficientD(stockCode1, stockCode2, begin,
				end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getInfluencePie")
	public PieVO getInfluencePie(
			@RequestParam("stockCodes") Collection<String> stockCodes,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getInfluencePie(stockCodes, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getIndustryInfluencePie")
	public PieVO getIndustryInfluencePie(
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getIndustryInfluencePie(begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getTrend/D")
	public Collection<PointVO> getTrendD(
			@RequestParam("stockCode") String stockCode,
			@RequestParam("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
			@RequestParam("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end)
			throws NetworkConnectionException {
		return impl.getTrendD(stockCode, begin, end);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getComment")
	public String getComment(@RequestParam("stockCode") String stockCode)
			throws NetworkConnectionException {
		return impl.getComment(stockCode);
	}

	/**
	 * 获取指定股票的五维指标
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getStockQuota")
	public QuotaVO getStockQuota(@RequestParam("stockCode") String stockCode)
			throws NetworkConnectionException {
		return impl.getStockQuota(stockCode);
	}

	/**
	 * 获得两只股票的距离
	 */
	@Override
	@ResponseBody
	@RequestMapping("/getDistance")
	public double getDistance(
			@RequestParam("firstCode") String stockCode,
			@RequestParam("secondCode") String otherStock)
			throws NetworkConnectionException {
		return impl.getDistance(stockCode,otherStock);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getStockQuotas")
	public Collection<QuotaVO> getStockQuotas(
			@RequestParam("stockCodes") List<String> codes)
			throws NetworkConnectionException {
		return impl.getStockQuotas(codes);
	}

	@Override
	@ResponseBody
	@RequestMapping("/getClosestQuota")
	public Collection<QuotaVO> getClosestQuota(@RequestParam("stockCode") String stockCode)
			throws NetworkConnectionException {
		return impl.getClosestQuota(stockCode);
	}

	@Override
	@ResponseBody
	@RequestMapping("/setPercentage")
	public boolean setPercentage(
			@RequestParam("percentage") VectorVO percentage,
			@RequestParam("target") QuotaVO target) {
		return impl.setPercentage(percentage,target);
	}


}
