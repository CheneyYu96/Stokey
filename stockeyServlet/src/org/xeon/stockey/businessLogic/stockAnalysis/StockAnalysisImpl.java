package org.xeon.stockey.businessLogic.stockAnalysis;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.xeon.stockey.businessLogic.stockAccess.FileBenchmarkSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.StockDailyDataFilterImpl;
import org.xeon.stockey.businessLogic.utility.DateTypeConverter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogicService.stockAccessService.StockDailyDataFilterService;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.businessLogicService.stockAnalysisService.StockAnalysisService;
import org.xeon.stockey.util.WithLocalDate;
import org.xeon.stockey.vo.*;

/**
 * StockAnalysisService的实现
 * Created by Sissel on 2016/4/6.
 */
public class StockAnalysisImpl implements StockAnalysisService
{
    private StockSourceService stockSourceService = new FileStockSourceImpl();
    private StockSourceService benchmarkSourceService = new FileBenchmarkSourceImpl();

    enum AcquireType
    {
        DAY,
        WEEK,
        MONTH
    }

    private <E extends WithLocalDate> Collection<E> filterDates(Collection<E> source, LocalDate begin, LocalDate end)
    {
        begin = begin.minusDays(1);
        end = end.plusDays(1);

        LinkedList<E> result = new LinkedList<>();
        for (E e : source)
        {
            LocalDate date = e.getDate();
            if (date.isBefore(end) && date.isAfter(begin))
            {
                result.add(e);
            }
        }

        return result;
    }

    private Collection<DailyDataVO> getDailyDataVOs(String stockCode, LocalDate begin, LocalDate end, AcquireType type, int moreDays)
            throws NetworkConnectionException
    {
        switch (type)
        {
            case DAY:
                return this.getDailyDataVOsD(stockCode, begin, end, moreDays);
            case WEEK:
                return this.getDailyDataVOsW(stockCode, begin, end, moreDays);
            case MONTH:
                return this.getDailyDataVOsM(stockCode, begin, end, moreDays);
        }

        return null;
    }

    private Collection<DailyDataVO> getDailyDataVOsD(String stockCode, LocalDate begin, LocalDate end, int moreDays)
            throws NetworkConnectionException
    {
        StockSourceService source;
        // TODO: 2016/4/15 hard code
        if (stockCode.startsWith("bm"))
        {
            source = benchmarkSourceService;
        }
        else
        {
            source = stockSourceService;
        }

        StockDailyDataFilterService filterService = new StockDailyDataFilterImpl
                (source.getStockInfo(stockCode), source);

        begin = begin.minusDays(moreDays);
        return filterService.filterDate(begin, end).getResult();
    }

    // 如果结束日期不是周五，改成上周五
    private Collection<DailyDataVO> getDailyDataVOsW(String stockCode, LocalDate begin, LocalDate end, int moreDays)
            throws NetworkConnectionException
    {
        if (end.getDayOfWeek() != DayOfWeek.FRIDAY && end.getDayOfWeek().getValue() < DayOfWeek.FRIDAY.getValue() )
        {
            end = end.minusDays(2 + end.getDayOfWeek().getValue());
        }

        return this.getDailyDataVOsD(stockCode, begin, end, moreDays);
    }

    // 如果结束日期不是月底，改成上月底
    private Collection<DailyDataVO> getDailyDataVOsM(String stockCode, LocalDate begin, LocalDate end, int moreDays)
            throws NetworkConnectionException
    {
        if (end.plusDays(1).getDayOfMonth() != 1) // 非月底
        {
            end = end.withDayOfMonth(1); // 到1号
            end = end.minusDays(1);
        }

        return this.getDailyDataVOsD(stockCode, begin, end, moreDays);
    }

    // the returned CandlestickVO's date field is the date of the first element
    private CandlestickVO mergeCandleStick(Collection<DailyDataVO> dailyDataVOs)
    {
        double high = dailyDataVOs
                .stream()
                .map(DailyDataVO::getHigh)
                .max(Double::compare)
                .get();

        double low = dailyDataVOs
                .stream()
                .map(DailyDataVO::getLow)
                .min(Double::compare)
                .get();

        DailyDataVO first, last;
        first = dailyDataVOs
                .stream()
                .findFirst()
                .get();

        Iterator<DailyDataVO> iterator = dailyDataVOs.iterator();
        last = first;   // prevent only one element in the list
        while (iterator.hasNext())
        {
            last = iterator.next();
        }

        return new CandlestickVO(first.getOpen(), last.getClose(), high, low, first.getDate());
    }

    private boolean inSameWeek(LocalDate before, LocalDate after)
    {

        return before.until(after).getDays() < 6
                && before.getDayOfWeek().getValue() < after.getDayOfWeek().getValue();
    }

    private boolean inSameMonth(LocalDate before, LocalDate after)
    {
        return before.getMonth() == after.getMonth() && before.getYear() == after.getYear();
    }

    interface Classifier
    {
        boolean inSamePeriod(LocalDate before, LocalDate after);
    }

    /**
     * @param dailyDataVOs 需要分类的DailyDataVO集合
     * @param classifier 如何分类（根据月还是周）
     * @return 分好的列表的集合
     */
    private Collection<Collection<DailyDataVO>> classify (Collection<DailyDataVO> dailyDataVOs, Classifier classifier)
    {
        Collection<Collection<DailyDataVO>> result = new LinkedList<>();
        Collection<DailyDataVO> currentCollect = new LinkedList<>();
        LocalDate lastDate = null;
        result.add(currentCollect);

        for (DailyDataVO dailyDataVO : dailyDataVOs)
        {
            LocalDate currentDate = dailyDataVO.getDate();
            if(lastDate == null)
            {
                lastDate = currentDate;
                currentCollect.add(dailyDataVO);
            }
            else
            {
                if(classifier.inSamePeriod(lastDate, currentDate))
                {
                    currentCollect.add(dailyDataVO);
                    lastDate = currentDate;
                }
                else
                {
                    currentCollect = new LinkedList<>();
                    currentCollect.add(dailyDataVO);
                    lastDate = currentDate;
                    result.add(currentCollect);
                }
            }
        }

        return result;
    }

    private Collection<Collection<DailyDataVO>> classifyInWeeks(Collection<DailyDataVO> dailyDataVOs)
    {
        return classify(dailyDataVOs, this::inSameWeek);
    }

    private Collection<Collection<DailyDataVO>> classifyInMonths(Collection<DailyDataVO> dailyDataVOs)
    {
        return classify(dailyDataVOs, this::inSameMonth);
    }

    private Collection<CandlestickVO> getCandlestickLine
            (String stockCode, LocalDate begin, LocalDate end, AcquireType acquireType, int moreDays,
             Function<Collection<DailyDataVO>, Collection<CandlestickVO>> mapper) throws NetworkConnectionException
    {
        Collection<DailyDataVO> vos = this.getDailyDataVOs(stockCode, begin, end, acquireType, moreDays);
        Collection<CandlestickVO> candles = mapper.apply(vos);

        // assign ma5, ma10, ma20, ma30
        List<Double> closes = candles
                .stream()
                .mapToDouble(o -> o.close)
                .boxed()
                .collect(Collectors.toList());
        ArrayList<Double> ma5s = MACalculator.ma(closes, 5);
        ArrayList<Double> ma10s = MACalculator.ma(closes, 10);
        ArrayList<Double> ma20s = MACalculator.ma(closes, 20);
        ArrayList<Double> ma30s = MACalculator.ma(closes, 30);
        Iterator<CandlestickVO> iterator = candles.iterator();
        int cnt = 0;
        while (iterator.hasNext())
        {
            CandlestickVO vo = iterator.next();
            vo.ma5 = ma5s.get(cnt);
            vo.ma10 = ma10s.get(cnt);
            vo.ma20 = ma20s.get(cnt);
            vo.ma30 = ma30s.get(cnt);
            ++cnt;
        }

        // filter because the acquired data have more than required
        return this.filterDates(candles, begin, end);
    }

    @Override
    public Collection<CandlestickVO> getCandlestickLineD(String stockCode, LocalDate begin, LocalDate end)
            throws NetworkConnectionException
    {
        return this.getCandlestickLine(stockCode, begin, end, AcquireType.DAY, 100,
                vos -> vos.stream().
                                map(vo -> new CandlestickVO
                                        (vo.getOpen(), vo.getClose(), vo.getHigh(), vo.getLow(), vo.getDate()))
                                .collect(Collectors.toCollection(LinkedList::new)));
    }

    @Override
    public Collection<CandlestickVO> getCandlestickLineW(String stockCode, LocalDate begin, LocalDate end)
            throws NetworkConnectionException
    {
        return this.getCandlestickLine(stockCode, begin, end, AcquireType.WEEK, 700,
                vos -> classifyInWeeks(vos)
                        .stream()
                        .map(this::mergeCandleStick)
                        .collect(Collectors.toList()));
    }

    @Override
    public Collection<CandlestickVO> getCandlestickLineM(String stockCode, LocalDate begin, LocalDate end)
            throws NetworkConnectionException
    {
        return this.getCandlestickLine(stockCode, begin, end, AcquireType.MONTH, 3000,
                vos -> classifyInMonths(vos)
                        .stream()
                        .map(this::mergeCandleStick)
                        .collect(Collectors.toList()));
    }

    private Collection<MacdVO> getMacd(List<Double> values, List<LocalDate> dateList)
    {
        ArrayList<Double> ema12 = MACalculator.ema(values, 12);
        ArrayList<Double> ema26 = MACalculator.ema(values, 26);

        ArrayList<Double> difs = new ArrayList<>(values.size());
        for (int i = 0; i < values.size(); i++)
        {
            difs.add(i, ema12.get(i) - ema26.get(i));
        }

        ArrayList<MacdVO> macdVOs = new ArrayList<>(values.size());
        ArrayList<Double> dems = MACalculator.ma(difs, 9);
        for (int i = 0; i < values.size(); i++)
        {
            MacdVO vo = new MacdVO(dateList.get(i));
            vo.dif = difs.get(i);
            vo.dem = dems.get(i);
            vo.osc = vo.dif - vo.dem;
            macdVOs.add(vo);
        }

        return macdVOs;
    }

    @Override
    public Collection<MacdVO> getMacdD(String stockCode, LocalDate begin, LocalDate end)
            throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsD(stockCode, begin, end, 100);

        List<Double> values = dailyDataVOs
                .stream()
                .mapToDouble(DailyDataVO::getClose)
                .boxed()
                .collect(Collectors.toList());

        List<LocalDate> dateList = dailyDataVOs
                .stream()
                .map(DailyDataVO::getDate)
                .collect(Collectors.toList());

        Collection<MacdVO> result = this.getMacd(values, dateList);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    /**
     * 把分组里面的汇总成一个东西
     * @param source 分组的集合
     * @param closes 汇总后的收盘价， 会通过改变这个参数来返回
     * @param dates 汇总后的日期，会通过改变这个参数来返回
     */
    private void fillInDatesAndCloses
            (Collection<Collection<DailyDataVO>> source, List<Double> closes, List<LocalDate> dates)
    {
        for (Collection<DailyDataVO> dailyDataSet : source)
        {
            Iterator<DailyDataVO> iterator = dailyDataSet.iterator();
            DailyDataVO first = iterator.next();
            dates.add(first.getDate());

            DailyDataVO last = first;
            while (iterator.hasNext())
            {
                last = iterator.next();
            }
            closes.add(last.getClose());
        }
    }

    @Override
    public Collection<MacdVO> getMacdW(String stockCode, LocalDate begin, LocalDate end)
            throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsW(stockCode, begin, end, 400);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classify(dailyDataVOs, this::inSameWeek);

        List<Double> values = new ArrayList<>(dailyDataSets.size());
        List<LocalDate> dateList = new ArrayList<>(dailyDataSets.size());

        this.fillInDatesAndCloses(dailyDataSets, values, dateList);

        Collection<MacdVO> result = this.getMacd(values, dateList);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    @Override
    public Collection<MacdVO> getMacdM(String stockCode, LocalDate begin, LocalDate end)
            throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsM(stockCode, begin, end, 800);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classify(dailyDataVOs, this::inSameMonth);

        List<Double> values = new ArrayList<>(dailyDataSets.size());
        List<LocalDate> dateList = new ArrayList<>(dailyDataSets.size());

        this.fillInDatesAndCloses(dailyDataSets, values, dateList);

        Collection<MacdVO> result = this.getMacd(values, dateList);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    private Collection<RsiVO> getRsi(List<Double> values, List<LocalDate> dates)
    {
        ArrayList<Double> rsi6 = RSICalculator.calculateRsi(values, 6);
        ArrayList<Double> rsi12 = RSICalculator.calculateRsi(values, 12);
        ArrayList<Double> rsi24 = RSICalculator.calculateRsi(values, 24);
        ArrayList<RsiVO> result = new ArrayList<>();

        int i = 0;
        for (LocalDate date : dates)
        {
            result.add(new RsiVO(date, rsi6.get(i), rsi12.get(i), rsi24.get(i)));
            ++i;
        }

        return result;
    }

    @Override
    public Collection<RsiVO> getRsiD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsD(stockCode, begin, end, 100);
        List<Double> values = dailyDataVOs
                .stream()
                .mapToDouble(DailyDataVO::getClose)
                .boxed()
                .collect(Collectors.toList());
        List<LocalDate> dateList = dailyDataVOs
                .stream()
                .map(DailyDataVO::getDate)
                .collect(Collectors.toList());

        Collection<RsiVO> result = getRsi(values, dateList);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    @Override
    public Collection<RsiVO> getRsiW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsW(stockCode, begin, end, 500);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classify(dailyDataVOs, this::inSameWeek);

        List<Double> values = new ArrayList<>(dailyDataSets.size());
        List<LocalDate> dateList = new ArrayList<>(dailyDataSets.size());

        this.fillInDatesAndCloses(dailyDataSets, values, dateList);

        Collection<RsiVO> result = getRsi(values, dateList);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    @Override
    public Collection<RsiVO> getRsiM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsM(stockCode, begin, end, 800);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classify(dailyDataVOs, this::inSameMonth);

        List<Double> values = new ArrayList<>(dailyDataSets.size());
        List<LocalDate> dateList = new ArrayList<>(dailyDataSets.size());

        this.fillInDatesAndCloses(dailyDataSets, values, dateList);

        Collection<RsiVO> result = getRsi(values, dateList);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    private Collection<KdjVO> getKdj
	(String stockCode, LocalDate begin, LocalDate end, AcquireType acquireType,
			Function<Collection<DailyDataVO>, Collection<DailyDataVO>> mapper) throws NetworkConnectionException
	{
		KDJCalculator kdjCal = new KDJCalculator();
		Collection<DailyDataVO> vos = this.getDailyDataVOs(stockCode, begin, end, acquireType, 100);
		Collection<DailyDataVO> dmvos =  mapper.apply(vos);

        Collection<KdjVO> result = kdjCal.calculateKDJ(dmvos);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
	}

	@Override
	public Collection<KdjVO> getKdjD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException {
		return this.getKdj(stockCode, begin, end, AcquireType.DAY, vos -> vos);
	}

	@Override
	public Collection<KdjVO> getKdjW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException {
		return this.getKdj(stockCode, begin, end, AcquireType.WEEK,
				vos ->
		{
			Iterator<Collection<DailyDataVO>> dvosit = this.classifyInWeeks(vos).iterator();
			Collection<DailyDataVO> result = new LinkedList<>();
			while(dvosit.hasNext()){
				Collection<DailyDataVO> dvos = dvosit.next();
				
				double high = dvos.stream()
						.map(DailyDataVO::getHigh)
						.max(Double::compare)
						.get();
				double low = dvos.stream()
						.map(DailyDataVO::getLow)
						.max(Double::compare)
						.get();
				double close = 0;
                for (DailyDataVO dvo : dvos){
                    close = dvo.getClose();
                }
				Calendar cal = DateTypeConverter.convert(dvos.iterator().next().getDate());
				String symbol = dvos.iterator().next().getSymbol();
				DailyDataVO vo = new DailyDataVO(symbol,cal,
						close,0,0,0,0,0,0,high,low);
				result.add(vo);
			}
			return result;
		});
	}

	@Override
	public Collection<KdjVO> getKdjM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException {
		return this.getKdj(stockCode, begin, end, AcquireType.MONTH,
				vos ->
		{
			Iterator<Collection<DailyDataVO>> dvosit = this.classifyInMonths(vos).iterator();
			Collection<DailyDataVO> result = new LinkedList<>();
			while(dvosit.hasNext()){
				Collection<DailyDataVO> dvos = dvosit.next();
				
				double high = dvos.stream()
						.map(DailyDataVO::getHigh)
						.max(Double::compare)
						.get();
				double low = dvos.stream()
						.map(DailyDataVO::getLow)
						.max(Double::compare)
						.get();
				double close = 0;
                for (DailyDataVO dvo : dvos) {
                    close = dvo.getClose();
                }
				Calendar cal = DateTypeConverter.convert(dvos.iterator().next().getDate());
				String symbol = dvos.iterator().next().getSymbol();
				DailyDataVO vo = new DailyDataVO(symbol,cal,
						close,0,0,0,0,0,0,high,low);
				result.add(vo);
			}
			return result;
		});
	}

    private Collection<ChangeVO> calculateChange(Collection<Double> values, Collection<LocalDate> dates)
    {
        ArrayList<ChangeVO> result = new ArrayList<>(values.size());
        double before = 1;

        Iterator<LocalDate> dateIterator = dates.iterator();

        for (Double value : values)
        {
            ChangeVO changeVO = new ChangeVO((value - before)/before, dateIterator.next());
            result.add(changeVO);
            before = value;
        }

        return result;
    }

    @Override
    public Collection<ChangeVO> getChangeD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsD(stockCode, begin, end, 10);
        List<Double> closes = dailyDataVOs
                .stream()
                .mapToDouble(DailyDataVO::getClose)
                .boxed()
                .collect(Collectors.toList());

        List<LocalDate> dateList = dailyDataVOs
                .stream()
                .map(DailyDataVO::getDate)
                .collect(Collectors.toList());

        Collection<ChangeVO> result = this.calculateChange(closes, dateList);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    @Override
    public Collection<ChangeVO> getChangeW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsW(stockCode, begin, end, 100);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classifyInWeeks(dailyDataVOs);

        List<Double> closes = new ArrayList<>(dailyDataSets.size());
        List<LocalDate> dateList = new ArrayList<>(dailyDataSets.size());

        this.fillInDatesAndCloses(dailyDataSets, closes, dateList);

        Collection<ChangeVO> result = this.calculateChange(closes, dateList);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    @Override
    public Collection<ChangeVO> getChangeM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsM(stockCode, begin, end, 100);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classifyInMonths(dailyDataVOs);

        List<Double> closes = new ArrayList<>(dailyDataSets.size());
        List<LocalDate> dateList = new ArrayList<>(dailyDataSets.size());

        this.fillInDatesAndCloses(dailyDataSets, closes, dateList);

        Collection<ChangeVO> result = this.calculateChange(closes, dateList);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    private Collection<VolumeVO> mergeVolume(Collection<Collection<DailyDataVO>> dailyDataSets)
    {
        ArrayList<VolumeVO> result = new ArrayList<>(dailyDataSets.size());

        for (Collection<DailyDataVO> dailyDataSet : dailyDataSets)
        {
            double volumeSum = 0;
            for (DailyDataVO dailyDataVO : dailyDataSet)
            {
                volumeSum += dailyDataVO.getVolumn();
            }

            result.add(new VolumeVO(
                    volumeSum,
                    dailyDataSet
                            .stream()
                            .findFirst()
                            .get()
                            .getDate()));
        }

        return result;
    }

    @Override
    public Collection<VolumeVO> getVolumeD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsD(stockCode, begin, end, 10);
        ArrayList<VolumeVO> volumeVOs = new ArrayList<>(dailyDataVOs.size());

        volumeVOs.addAll(dailyDataVOs
                .stream()
                .map(dailyDataVO -> new VolumeVO(dailyDataVO.getVolumn(), dailyDataVO.getDate()))
                .collect(Collectors.toList()));

        // filter because the acquired data have more than required
        return this.filterDates(volumeVOs, begin, end);
    }

    @Override
    public Collection<VolumeVO> getVolumeW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsW(stockCode, begin, end, 10);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classifyInWeeks(dailyDataVOs);

        Collection<VolumeVO> result = this.mergeVolume(dailyDataSets);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    @Override
    public Collection<VolumeVO> getVolumeM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsM(stockCode, begin, end, 10);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classifyInMonths(dailyDataVOs);

        Collection<VolumeVO> result = this.mergeVolume(dailyDataSets);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    private double getVarianceOfChange(Collection<ChangeVO> changeVOs)
    {
        Collection<Double> values = changeVOs
                .stream()
                .mapToDouble( o -> o.change)
                .boxed()
                .collect(Collectors.toList());

        return VarianceCalculator.calculateVariance(values);
    }

    @Override
    public double getVarianceOfChangeD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<ChangeVO> changeVOs = this.getChangeD(stockCode, begin, end);

        return this.getVarianceOfChange(changeVOs);
    }

    private double getVarianceOfChangeW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<ChangeVO> changeVOs = this.getChangeW(stockCode, begin, end);

        return this.getVarianceOfChange(changeVOs);
    }

    private double getVarianceOfChangeM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<ChangeVO> changeVOs = this.getChangeM(stockCode, begin, end);

        return this.getVarianceOfChange(changeVOs);
    }

    private double getCovarianceOfChange(Collection<ChangeVO> changeVOs1, Collection<ChangeVO> changeVOs2)
    {
        Collection<Double> values1 = changeVOs1
                .stream()
                .mapToDouble( o -> o.change)
                .boxed()
                .collect(Collectors.toList());

        Collection<Double> values2 = changeVOs2
                .stream()
                .mapToDouble( o -> o.change)
                .boxed()
                .collect(Collectors.toList());

        return VarianceCalculator.calculateCovariance(values1, values2);
    }

    @Override
    public double getCovarianceOfChangeD(String stockCode1, String stockCode2, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<ChangeVO> changeVOs1 = this.getChangeD(stockCode1, begin, end);
        Collection<ChangeVO> changeVOs2 = this.getChangeD(stockCode2, begin, end);

        return this.getCovarianceOfChange(changeVOs1, changeVOs2);
    }

    private double getCovarianceOfChangeW(String stockCode1, String stockCode2, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<ChangeVO> changeVOs1 = this.getChangeW(stockCode1, begin, end);
        Collection<ChangeVO> changeVOs2 = this.getChangeW(stockCode2, begin, end);

        return this.getCovarianceOfChange(changeVOs1, changeVOs2);
    }

    private double getCovarianceOfChangeM(String stockCode1, String stockCode2, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<ChangeVO> changeVOs1 = this.getChangeM(stockCode1, begin, end);
        Collection<ChangeVO> changeVOs2 = this.getChangeM(stockCode2, begin, end);

        return this.getCovarianceOfChange(changeVOs1, changeVOs2);
    }

    @Override
    public PieVO getInfluencePie(Collection<String> stockCodes, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        long num = 1000;

        Map<String, Double> afterWeights = new HashMap<>();

        double sum = 0;

        for (String stockCode : stockCodes)
        {
            Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsD(stockCode, begin, end, 0);
            Iterator<DailyDataVO> iterator = dailyDataVOs.iterator();

            DailyDataVO first = iterator.next();
            DailyDataVO lastOne = first;
            while (iterator.hasNext())
            {
                lastOne = iterator.next();
            }

            double afterWeight = Math.abs(num * (lastOne.getClose() - first.getClose()));
            sum += afterWeight;
            afterWeights.put(stockCode, afterWeight);
        }

        PieVO pieVO = new PieVO("占收入影响饼状图", begin, end);
        StockSourceService stockSourceService = new FileStockSourceImpl();

        for (String stockCode : stockCodes)
        {
            double share = afterWeights.get(stockCode) / sum;
            pieVO.putStock(stockSourceService.getStockInfo(stockCode), share);
        }

        return pieVO;
    }

    @Override
	public PieVO getIndustryInfluencePie(LocalDate begin, LocalDate end) throws NetworkConnectionException {
		ArrayList<String> list = new ArrayList<>();
		list.add("sh600036");
		list.add("sz002142");
		list.add("sh601398");
		list.add("sh601288");
		list.add("sh600606");
		list.add("sh600048");
		list.add("sz000031");
		list.add("sz002332");
		list.add("sz000402");
		PieVO re = this.getInfluencePie(list, begin, end);
		Map<String,Double> old = re.shareMap;
		Map<String,Double> result = new HashMap<>();
		
		//bank stock
		double shareBank = 0;
		shareBank += old.get("sh600036");
		shareBank += old.get("sz002142");
		shareBank += old.get("sh601398");
		shareBank += old.get("sh601288");
		//estate stock
		double shareEstate = 0;
		shareEstate += old.get("sh600606");
		shareEstate += old.get("sh600048");
		shareEstate += old.get("sz000031");
		shareEstate += old.get("sz000402");
		
		//medicine stock
		double shareMed = 0;
		shareMed += old.get("sz002332");
		
		result.put("银行行业", shareBank);
		result.put("房地产行业", shareEstate);
		result.put("制药行业", shareMed);
		
		re.shareMap = result;
		return re;
	}

    private double getCorrelationCoefficient(Collection<ChangeVO> changeVOs1, Collection<ChangeVO> changeVOs2)
    {
        Collection<Double> values1 = changeVOs1
                .stream()
                .mapToDouble( o -> o.change)
                .boxed()
                .collect(Collectors.toList());

        Collection<Double> values2 = changeVOs2
                .stream()
                .mapToDouble( o -> o.change)
                .boxed()
                .collect(Collectors.toList());

        return VarianceCalculator.calculateCorrelationCoefficient(values1, values2);
    }

    @Override
    public double getCorrelationCoefficientD(String stockCode1, String stockCode2, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<ChangeVO> changeVOs1 = this.getChangeD(stockCode1, begin, end);
        Collection<ChangeVO> changeVOs2 = this.getChangeD(stockCode2, begin, end);

        return this.getCorrelationCoefficient(changeVOs1, changeVOs2);
    }

    private double getCorrelationCoefficientW(String stockCode1, String stockCode2, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<ChangeVO> changeVOs1 = this.getChangeW(stockCode1, begin, end);
        Collection<ChangeVO> changeVOs2 = this.getChangeW(stockCode2, begin, end);

        return this.getCorrelationCoefficient(changeVOs1, changeVOs2);
    }

    private double getCorrelationCoefficientM(String stockCode1, String stockCode2, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<ChangeVO> changeVOs1 = this.getChangeM(stockCode1, begin, end);
        Collection<ChangeVO> changeVOs2 = this.getChangeM(stockCode2, begin, end);

        return this.getCorrelationCoefficient(changeVOs1, changeVOs2);
    }

    private Collection<PointVO> mergeTurnover(Collection<Collection<DailyDataVO>> dailyDataSets)
    {
        ArrayList<PointVO> result = new ArrayList<>(dailyDataSets.size());

        for (Collection<DailyDataVO> dailyDataSet : dailyDataSets)
        {
            double volumeSum = 0;
            DailyDataVO end = null;
            for (DailyDataVO dailyDataVO : dailyDataSet)
            {
                volumeSum += dailyDataVO.getVolumn();
                end = dailyDataVO;
            }
            double stocksNum = end.getVolumn() / end.getTurnover();

            result.add(new PointVO(
                    volumeSum / stocksNum,
                    dailyDataSet
                            .stream()
                            .findFirst()
                            .get()
                            .getDate()));
        }

        return result;
    }

    @Override
    public Collection<PointVO> getTurnoverD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsD(stockCode, begin, end, 10);
        ArrayList<PointVO> turnovers = new ArrayList<>(dailyDataVOs.size());

        turnovers.addAll(dailyDataVOs
                .stream()
                .map(dailyDataVO -> new PointVO(dailyDataVO.getTurnover(), dailyDataVO.getDate()))
                .collect(Collectors.toList()));

        // filter because the acquired data have more than required
        return this.filterDates(turnovers, begin, end);
    }

    @Override
    public Collection<PointVO> getTurnoverW(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsW(stockCode, begin, end, 10);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classifyInWeeks(dailyDataVOs);

        Collection<PointVO> result = this.mergeTurnover(dailyDataSets);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    @Override
    public Collection<PointVO> getTurnoverM(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsM(stockCode, begin, end, 10);
        Collection<Collection<DailyDataVO>> dailyDataSets = this.classifyInMonths(dailyDataVOs);

        Collection<PointVO> result = this.mergeTurnover(dailyDataSets);

        // filter because the acquired data have more than required
        return this.filterDates(result, begin, end);
    }

    @Override
    public Collection<PointVO> getTrendD(String stockCode, LocalDate begin, LocalDate end) throws NetworkConnectionException
    {
        Collection<DailyDataVO> dailyDataVOs = this.getDailyDataVOsD(stockCode, begin, end, 0);
        Collection<PointVO> result = new ArrayList<>(dailyDataVOs.size());

        Iterator<DailyDataVO> iterator = dailyDataVOs.iterator();

        DailyDataVO first = iterator.next();
        double base = first.getClose();
        result.add(new PointVO(0.0, first.getDate()));

        while (iterator.hasNext())
        {
            DailyDataVO dailyDataVO = iterator.next();
            double price = dailyDataVO.getClose();
            result.add(new PointVO((price - base) / base, dailyDataVO.getDate()));
        }

        return result;
    }
}
