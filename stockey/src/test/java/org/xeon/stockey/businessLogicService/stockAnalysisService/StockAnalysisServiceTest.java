package org.xeon.stockey.businessLogicService.stockAnalysisService;

import org.junit.Assert;
import org.junit.Test;
import org.xeon.stockey.businessLogic.stockAnalysis.StockAnalysisImpl;
import org.xeon.stockey.vo.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StockAnalysisService 的单元测试
 * Created by Sissel on 2016/4/15.
 */
public class StockAnalysisServiceTest
{
    StockAnalysisService stockAnalysisService = new StockAnalysisImpl();

    private List<Double> getListFromArray(double[] array)
    {
        ArrayList<Double> result = new ArrayList<>(array.length);
        for (double aDouble : array)
        {
            result.add(aDouble);
        }
        return result;
    }

    private void compareDoubleCollection(Collection<Double> expected, Collection<Double> actual, double delta)
    {
        Assert.assertEquals(expected.size(), actual.size());
        Iterator<Double> iterExpect = expected.iterator();
        Iterator<Double> iterActual = actual.iterator();
        while (iterActual.hasNext())
        {
            Assert.assertEquals(iterExpect.next(), iterActual.next(), delta);
        }
    }

    private void compareCandlestick(Collection<CandlestickVO> candlestickVOs,
                                    double[] opens, double[] closes, double[] highs, double[] lows,
                                    double[] ma5s, double[] ma10s, double[] ma20s, double[] ma30s)
    {
        Assert.assertEquals(opens.length, candlestickVOs.size());

        this.compareDoubleCollection(
                this.getListFromArray(opens),
                candlestickVOs.stream().mapToDouble(vo -> vo.open).boxed().collect(Collectors.toList()),
                0.01
        );
        this.compareDoubleCollection(
                this.getListFromArray(closes),
                candlestickVOs.stream().mapToDouble(vo -> vo.close).boxed().collect(Collectors.toList()),
                0.01
        );
        this.compareDoubleCollection(
                this.getListFromArray(highs),
                candlestickVOs.stream().mapToDouble(vo -> vo.high).boxed().collect(Collectors.toList()),
                0.01
        );
        this.compareDoubleCollection(
                this.getListFromArray(lows),
                candlestickVOs.stream().mapToDouble(vo -> vo.low).boxed().collect(Collectors.toList()),
                0.01
        );
        this.compareDoubleCollection(
                this.getListFromArray(ma5s),
                candlestickVOs.stream().mapToDouble(vo -> vo.ma5).boxed().collect(Collectors.toList()),
                0.01
        );
        this.compareDoubleCollection(
                this.getListFromArray(ma10s),
                candlestickVOs.stream().mapToDouble(vo -> vo.ma10).boxed().collect(Collectors.toList()),
                0.01
        );
        this.compareDoubleCollection(
            this.getListFromArray(ma20s),
            candlestickVOs.stream().mapToDouble(vo -> vo.ma20).boxed().collect(Collectors.toList()),
            0.01
        );
        this.compareDoubleCollection(
                this.getListFromArray(ma30s),
                candlestickVOs.stream().mapToDouble(vo -> vo.ma30).boxed().collect(Collectors.toList()),
                0.01
        );

    }

    private void compareMacd(Collection<MacdVO> macdVOs,
                             double[] dif, double[] dea, double[] osc)
    {
        Assert.assertEquals(dif.length, macdVOs.size());

        this.compareDoubleCollection(
                this.getListFromArray(dif),
                macdVOs.stream().mapToDouble(vo -> vo.dif).boxed().collect(Collectors.toList()),
                0.01
        );
        this.compareDoubleCollection(
                this.getListFromArray(dea),
                macdVOs.stream().mapToDouble(vo -> vo.dem).boxed().collect(Collectors.toList()),
                0.01
        );
        this.compareDoubleCollection(
                this.getListFromArray(osc),
                macdVOs.stream().mapToDouble(vo -> vo.osc).boxed().collect(Collectors.toList()),
                0.02
        );
    }

    private void compareRsi(Collection<RsiVO> rsiVOs,
                             double[] rsi6, double[] rsi12, double[] rsi24)
    {
        Assert.assertEquals(rsi6.length, rsiVOs.size());

        this.compareDoubleCollection(
                this.getListFromArray(rsi6),
                rsiVOs.stream().mapToDouble(vo -> vo.rsi6).boxed().collect(Collectors.toList()),
                1
        );
        this.compareDoubleCollection(
                this.getListFromArray(rsi12),
                rsiVOs.stream().mapToDouble(vo -> vo.rsi12).boxed().collect(Collectors.toList()),
                1
        );
        this.compareDoubleCollection(
                this.getListFromArray(rsi24),
                rsiVOs.stream().mapToDouble(vo -> vo.rsi24).boxed().collect(Collectors.toList()),
                1
        );
    }

    private void compareKdj(Collection<KdjVO> kdjVOs,
                            double[] kValues, double[] dValues, double[] jValues)
    {
        Assert.assertEquals(kValues.length, kdjVOs.size());

        this.compareDoubleCollection(
                this.getListFromArray(kValues),
                kdjVOs.stream().mapToDouble(vo -> vo.kValue).boxed().collect(Collectors.toList()),
                1
        );
        this.compareDoubleCollection(
                this.getListFromArray(dValues),
                kdjVOs.stream().mapToDouble(vo -> vo.dValue).boxed().collect(Collectors.toList()),
                1
        );
        this.compareDoubleCollection(
                this.getListFromArray(jValues),
                kdjVOs.stream().mapToDouble(vo -> vo.jValue).boxed().collect(Collectors.toList()),
                1
        );
    }

    @Test
    public void testGetCandlestickLineD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 5);
        double[] expectedOpen = {4.58, 4.42}; // from net
        double[] expectedClose = {4.45, 4.47};
        double[] expectedHigh = {4.59, 4.51};
        double[] expectedLow = {4.44, 4.38};
        double[] expectedMa5 = {4.57, 4.54};
        double[] expectedMa10 = {4.62, 4.60};
        double[] expectedMa20 = {4.62, 4.61};
        double[] expectedMa30 = {4.63, 4.63};

        Collection<CandlestickVO> candlestickVOs = stockAnalysisService.getCandlestickLineD("sh601398", begin, end);
        this.compareCandlestick(candlestickVOs,
                expectedOpen, expectedClose, expectedHigh, expectedLow,
                expectedMa5, expectedMa10, expectedMa20, expectedMa30);
    }

    @Test
    public void testGetCandlestickLineW() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 9);
        double[] expectedOpen = {4.58}; // from net
        double[] expectedClose = {4.46};
        double[] expectedHigh = {4.59};
        double[] expectedLow = {4.38};
        double[] expectedMa5 = {4.58};
        double[] expectedMa10 = {4.61};
        double[] expectedMa20 = {4.55};
        double[] expectedMa30 = {4.70};
        Collection<CandlestickVO> candlestickVOs = stockAnalysisService.getCandlestickLineW("sh601398", begin, end);
        this.compareCandlestick(candlestickVOs,
                expectedOpen, expectedClose, expectedHigh, expectedLow,
                expectedMa5, expectedMa10, expectedMa20, expectedMa30);
    }

    @Test
    public void testGetCandlestickLineM() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 1);
        LocalDate end = LocalDate.of(2016, 2, 1);
        double[] expectedOpen = {4.58}; // from net
        double[] expectedClose = {4.10};
        double[] expectedHigh = {4.59};
        double[] expectedLow = {4.02};
        double[] expectedMa5 = {4.44};
        double[] expectedMa10 = {4.70};
        double[] expectedMa20 = {4.37};
        double[] expectedMa30 = {4.12};
        Collection<CandlestickVO> candlestickVOs = stockAnalysisService.getCandlestickLineM("sh601398", begin, end);
        this.compareCandlestick(candlestickVOs,
                expectedOpen, expectedClose, expectedHigh, expectedLow,
                expectedMa5, expectedMa10, expectedMa20, expectedMa30);
    }

    @Test
    public void testGetMacdD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 11);
        LocalDate end = LocalDate.of(2016, 1, 13);
        Collection<MacdVO> macdVOs = stockAnalysisService.getMacdD("sh601398", begin, end);
        double[] expectedDiff = {-0.06, -0.07, -0.08}; // from net
        double[] expectedDea = {-0.03, -0.04, -0.05}; // from net
        double[] expectedOsc = {-0.03, -0.03, -0.03}; // calculated
        this.compareMacd(macdVOs, expectedDiff, expectedDea, expectedOsc);
    }

    @Test
    public void testGetMacdW() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 23);
        LocalDate end = LocalDate.of(2016, 2, 5);
        Collection<MacdVO> macdVOs = stockAnalysisService.getMacdW("sh601398", begin, end);
        double[] expectedDiff = {-0.12, -0.15}; // from net
        double[] expectedDea = {-0.08, -0.09}; // from net
        double[] expectedOsc = {-0.04, -0.06}; // calculated
        this.compareMacd(macdVOs, expectedDiff, expectedDea, expectedOsc);
    }

    @Test
    public void testGetMacdM() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 1);
        LocalDate end = LocalDate.of(2016, 3, 2);
        Collection<MacdVO> macdVOs = stockAnalysisService.getMacdM("sh601398", begin, end);
        double[] expectedDiff = {0.17, 0.12}; // from net
        double[] expectedDea = {0.29, 0.26}; // from net
        double[] expectedOsc = {-0.12, -0.14}; // calculated
        this.compareMacd(macdVOs, expectedDiff, expectedDea, expectedOsc);
    }

    @Test
    public void testGetRsiD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 3, 21);
        LocalDate end = LocalDate.of(2016, 3, 22);
        Collection<RsiVO> rsiVOs = stockAnalysisService.getRsiD("sh601398", begin, end);
        double[] expectedRsi6 = {42.12, 47.11}; // from net
        double[] expectedRsi12 = {65.02, 48.36}; // from net
        double[] expectedRsi24 = {63.87, 63.09}; // from net
        this.compareRsi(rsiVOs, expectedRsi6, expectedRsi12, expectedRsi24);
    }

    @Test
    public void testGetRsiW() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 3, 19);
        LocalDate end = LocalDate.of(2016, 4, 1);
        Collection<RsiVO> rsiVOs = stockAnalysisService.getRsiW("sh601398", begin, end);
        double[] expectedRsi6 = {83.72, 85.0}; // from net
        double[] expectedRsi12 = {32.65, 36.13}; // from net
        double[] expectedRsi24 = {48.63, 46.22}; // from net
        this.compareRsi(rsiVOs, expectedRsi6, expectedRsi12, expectedRsi24);
    }

    @Test
    public void testGetRsiM() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 3, 1);
        LocalDate end = LocalDate.of(2016, 5, 1);
        Collection<RsiVO> rsiVOs = stockAnalysisService.getRsiM("sh601398", begin, end);
        double[] expectedRsi6 = {48.97, 34.33}; // from net
        double[] expectedRsi12 = {41.06, 31.13}; // from net
        double[] expectedRsi24 = {56.89, 56.76}; // from net
        this.compareRsi(rsiVOs, expectedRsi6, expectedRsi12, expectedRsi24);
    }

    @Test
    public void testGetKdjD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 5);
        double[] expectedK = {30.12, 43.46}; // from net
        double[] expectedD = {41.89, 42.34};
        double[] expectedJ = {5.19, 44.34};
        Collection<KdjVO> kdjVOs = stockAnalysisService.getKdjD("sh601398", begin, end);
        this.compareKdj(kdjVOs, expectedK, expectedD, expectedJ);
    }

    @Test
    public void testGetKdjW() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 16);
        double[] expectedK = {-6.78, -27.41}; // from net
        double[] expectedD = {-13.69, -19.01};
        double[] expectedJ = {7.52, -44.34};
        Collection<KdjVO> kdjVOs = stockAnalysisService.getKdjW("sh601398", begin, end);
        this.compareKdj(kdjVOs, expectedK, expectedD, expectedJ);
    }

    @Test
    public void testGetKdjM() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 1);
        LocalDate end = LocalDate.of(2016, 3, 1);
        double[] expectedK = {-134.98, -94.02}; // from net
        double[] expectedD = {-94.10, -93.85};
        double[] expectedJ = {-217.33, -95.17};
        Collection<KdjVO> kdjVOs = stockAnalysisService.getKdjM("sh601398", begin, end);
        this.compareKdj(kdjVOs, expectedK, expectedD, expectedJ);
    }

    @Test
    public void testGetChangeD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        double[] expected = {-0.0284, 0.0045, 0.0089, -0.0177, 0.0068}; // from net
        Collection<ChangeVO> volumeVOs = stockAnalysisService.getChangeD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2016, 1, 2);
        end = LocalDate.of(2016, 1, 8);
        double[] expected2 = {-0.0284, 0.0045, 0.0089, -0.0177, 0.0068}; // from net
        volumeVOs = stockAnalysisService.getChangeD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 8);
        double[] expected3 = {-0.0284, 0.0045, 0.0089, -0.0177, 0.0068}; // from net
        volumeVOs = stockAnalysisService.getChangeD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected3),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 11);
        double[] expected4 = {-0.0284, 0.0045, 0.0089, -0.0177, 0.0068, -0.0314}; // from net
        volumeVOs = stockAnalysisService.getChangeD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected4),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);
    }

    @Test
    public void testGetChangeW() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        double[] expected = {-0.0262}; // from net
        Collection<ChangeVO> volumeVOs = stockAnalysisService.getChangeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2016, 1, 2);
        end = LocalDate.of(2016, 1, 8);
        double[] expected2 = {-0.0262}; // from net
        volumeVOs = stockAnalysisService.getChangeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 8);
        double[] expected3 = {-0.0262}; // from net
        volumeVOs = stockAnalysisService.getChangeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected3),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 15);
        double[] expected4 = {-0.0262, -0.0516}; // from net
        volumeVOs = stockAnalysisService.getChangeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected4),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 16);
        double[] expected5 = {-0.0262, -0.0516}; // from net
        volumeVOs = stockAnalysisService.getChangeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected5),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 19);
        double[] expected6 = {-0.0262, -0.0516}; // from net
        volumeVOs = stockAnalysisService.getChangeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected6),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);
    }

    @Test
    public void testGetChangeM() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 1);
        LocalDate end = LocalDate.of(2016, 1, 31);
        double[] expected = { -0.1048 }; // from net
        Collection<ChangeVO> volumeVOs = stockAnalysisService.getChangeM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 2, 4);
        double[] expected2 = { -0.1048 }; // from net
        volumeVOs = stockAnalysisService.getChangeM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2015, 12, 21);
        end = LocalDate.of(2016, 2, 1);
        double[] expected3 = { -0.1048 }; // from net
        volumeVOs = stockAnalysisService.getChangeM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected3),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);

        begin = LocalDate.of(2015, 12, 21);
        end = LocalDate.of(2016, 3, 3);
        double[] expected4 = { -0.1048, -0.0171 }; // from net
        volumeVOs = stockAnalysisService.getChangeM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected4),
                volumeVOs.stream().mapToDouble(o->o.change).boxed().collect(Collectors.toList()),
                0.0001);
    }

    @Test
    public void testGetVolumeD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        double[] expected = {147508685, 160629284, 113177263, 34491508, 186134474}; // from net
        Collection<VolumeVO> volumeVOs = stockAnalysisService.getVolumeD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                100);

        begin = LocalDate.of(2016, 1, 2);
        end = LocalDate.of(2016, 1, 8);
        double[] expected2 = {147508685, 160629284, 113177263, 34491508, 186134474}; // from net
        volumeVOs = stockAnalysisService.getVolumeD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                100);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 8);
        double[] expected3 = {147508685, 160629284, 113177263, 34491508, 186134474}; // from net
        volumeVOs = stockAnalysisService.getVolumeD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected3),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                100);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 11);
        double[] expected4 = {147508685, 160629284, 113177263, 34491508, 186134474, 164702051}; // from net
        volumeVOs = stockAnalysisService.getVolumeD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected4),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                100);
    }

    @Test
    public void testGetVolumeW() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        double[] expected = {641941214}; // from net
        Collection<VolumeVO> volumeVOs = stockAnalysisService.getVolumeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                500);

        begin = LocalDate.of(2016, 1, 2);
        end = LocalDate.of(2016, 1, 8);
        double[] expected2 = {641941214}; // from net
        volumeVOs = stockAnalysisService.getVolumeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                500);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 8);
        double[] expected3 = {641941214}; // from net
        volumeVOs = stockAnalysisService.getVolumeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected3),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                500);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 15);
        double[] expected4 = {641941214, 614239078}; // from net
        volumeVOs = stockAnalysisService.getVolumeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected4),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                500);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 16);
        double[] expected5 = {641941214, 614239078}; // from net
        volumeVOs = stockAnalysisService.getVolumeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected5),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                500);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 19);
        double[] expected6 = {641941214, 614239078}; // from net
        volumeVOs = stockAnalysisService.getVolumeW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected6),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                500);
    }

    @Test
    public void testGetVolumeM() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 1);
        LocalDate end = LocalDate.of(2016, 1, 31);
        double[] expected = { 2305097046.0 }; // from net
        Collection<VolumeVO> volumeVOs = stockAnalysisService.getVolumeM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                3000);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 2, 4);
        double[] expected2 = { 2305097046.0 }; // from net
        volumeVOs = stockAnalysisService.getVolumeM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                3000);

        begin = LocalDate.of(2015, 12, 21);
        end = LocalDate.of(2016, 2, 1);
        double[] expected3 = { 2305097046.0 }; // from net
        volumeVOs = stockAnalysisService.getVolumeM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected3),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                3000);

        begin = LocalDate.of(2015, 12, 21);
        end = LocalDate.of(2016, 3, 3);
        double[] expected4 = { 2305097046.0, 1625316255 }; // from net
        volumeVOs = stockAnalysisService.getVolumeM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected4),
                volumeVOs.stream().mapToDouble(o->o.volume).boxed().collect(Collectors.toList()),
                3000);
    }

    @Test
    public void testGetTurnoverD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        double[] expected = {0.05, 0.06, 0.04, 0.01, 0.07}; // from net
        Collection<PointVO> volumeVOs = stockAnalysisService.getTurnoverD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0);

        begin = LocalDate.of(2016, 1, 2);
        end = LocalDate.of(2016, 1, 8);
        double[] expected2 = {0.05, 0.06, 0.04, 0.01, 0.07}; // from net
        volumeVOs = stockAnalysisService.getTurnoverD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 8);
        double[] expected3 = {0.05, 0.06, 0.04, 0.01, 0.07}; // from net
        volumeVOs = stockAnalysisService.getTurnoverD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected3),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 11);
        double[] expected4 = {0.05, 0.06, 0.04, 0.01, 0.07, 0.06}; // from net
        volumeVOs = stockAnalysisService.getTurnoverD("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected4),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0);
    }

    @Test
    public void testGetTurnoverW() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        double[] expected = { 0.24 }; // from net
        Collection<PointVO> volumeVOs = stockAnalysisService.getTurnoverW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);

        begin = LocalDate.of(2016, 1, 2);
        end = LocalDate.of(2016, 1, 8);
        double[] expected2 = {0.24}; // from net
        volumeVOs = stockAnalysisService.getTurnoverW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 8);
        double[] expected3 = {0.24}; // from net
        volumeVOs = stockAnalysisService.getTurnoverW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected3),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 15);
        double[] expected4 = {0.24, 0.24}; // from net
        volumeVOs = stockAnalysisService.getTurnoverW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected4),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 16);
        double[] expected5 = {0.24, 0.24}; // from net
        volumeVOs = stockAnalysisService.getTurnoverW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected5),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 19);
        double[] expected6 = {0.24, 0.24}; // from net
        volumeVOs = stockAnalysisService.getTurnoverW("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected6),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);
    }

    @Test
    public void testGetTurnoverM() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 1);
        LocalDate end = LocalDate.of(2016, 1, 31);
        double[] expected = { 0.84 }; // from net
        Collection<PointVO> volumeVOs = stockAnalysisService.getTurnoverM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 2, 4);
        double[] expected2 = { 0.84 }; // from net
        volumeVOs = stockAnalysisService.getTurnoverM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);

        begin = LocalDate.of(2015, 12, 21);
        end = LocalDate.of(2016, 2, 1);
        double[] expected3 = { 0.84 }; // from net
        volumeVOs = stockAnalysisService.getTurnoverM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected3),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);

        begin = LocalDate.of(2015, 12, 21);
        end = LocalDate.of(2016, 3, 3);
        double[] expected4 = { 0.84, 0.62 }; // from net
        volumeVOs = stockAnalysisService.getTurnoverM("sh601398", begin, end);
        this.compareDoubleCollection(
                this.getListFromArray(expected4),
                volumeVOs.stream().mapToDouble(o->o.y).boxed().collect(Collectors.toList()),
                0.01);
    }

    @Test
    public void testGetVarianceOfChangeD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        double expected = 0.0002259936; // calculated
        double actual = stockAnalysisService.getVarianceOfChangeD("sh601398", begin, end);
        Assert.assertEquals(expected, actual, 0.00001);

        begin = LocalDate.of(2016, 1, 2);
        end = LocalDate.of(2016, 1, 8);
        double expected2 = 0.0002259936; // calculated
        double actual2 = stockAnalysisService.getVarianceOfChangeD("sh601398", begin, end);
        Assert.assertEquals(expected2, actual2, 0.00001);

        begin = LocalDate.of(2016, 1, 1);
        end = LocalDate.of(2016, 1, 11);
        double expected3 = 0.0002840492; // from net
        double actual3 = stockAnalysisService.getVarianceOfChangeD("sh601398", begin, end);
        Assert.assertEquals(expected3, actual3, 0.00001);
    }

    @Test
    public void testGetCovarianceOfChangeD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        double expected = 0.0002945456; // calculated
        double actual = stockAnalysisService.getCovarianceOfChangeD("sh601398", "sh601288", begin, end);
        Assert.assertEquals(expected, actual, 0.00001);

        begin = LocalDate.of(2016, 1, 4);
        end = LocalDate.of(2016, 1, 11);
        double expected2 = 0.0003110775; // calculated
        double actual2 = stockAnalysisService.getCovarianceOfChangeD("sh601398", "sh601288", begin, end);
        Assert.assertEquals(expected2, actual2, 0.00001);
    }

    @Test
    public void testGetCorrelationCoefficientD() throws Exception
    {
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 8);
        double expected = 0.973657011; // calculated
        double actual = stockAnalysisService.getCorrelationCoefficientD("sh601398", "sh601288", begin, end);
        Assert.assertEquals(expected, actual, 0.001);

        begin = LocalDate.of(2016, 1, 4);
        end = LocalDate.of(2016, 1, 11);
        double expected2 = 0.944203235; // calculated
        double actual2 = stockAnalysisService.getCorrelationCoefficientD("sh601398", "sh601288", begin, end);
        Assert.assertEquals(expected2, actual2, 0.001);
    }

    @Test
    public void testGetInfluencePie() throws Exception
    {
        ArrayList<String> codeList = new ArrayList<>();
        codeList.add("sh601288");
        codeList.add("sh601398");
        LocalDate begin = LocalDate.of(2016, 1, 4);
        LocalDate end = LocalDate.of(2016, 1, 11);
        PieVO pieVO = stockAnalysisService.getInfluencePie(codeList, begin, end);
        Assert.assertEquals(0.1875, pieVO.shareMap.get("sh601288"), 0.001);
        Assert.assertEquals(0.8125, pieVO.shareMap.get("sh601398"), 0.001);
    }

    @Test
    public void testGetTrendD() throws Exception
    {
        LocalDate begin = LocalDate.of(2015, 11, 19);
        LocalDate end = LocalDate.of(2015, 11, 23);
        Collection<PointVO> pointVOs = stockAnalysisService.getTrendD("sh601398", begin, end);
        double[] expected = {0, -0.0043, -0.0064};
        this.compareDoubleCollection(
                this.getListFromArray(expected),
                pointVOs.stream().mapToDouble(vo->vo.y).boxed().collect(Collectors.toList()),
                0.0001
        );

        begin = LocalDate.of(2015, 11, 19);
        end = LocalDate.of(2015, 11, 23);
        Collection<PointVO> pointVOs2 = stockAnalysisService.getTrendD("sh601288", begin, end);
        double[] expected2 = {0, -0.0031, -0.0093};
        this.compareDoubleCollection(
                this.getListFromArray(expected2),
                pointVOs2.stream().mapToDouble(vo->vo.y).boxed().collect(Collectors.toList()),
                0.0001
        );
    }

    @Test
    public void testGetIndustryInfluencePie() throws Exception
    {
        // 目前该方法只是作为一个demo演示，故先不写单元测试
    }

    @Test
    public void testGetComment() throws Exception
    {
        String comment = stockAnalysisService.getComment("sh601398");
        System.out.println(comment);
    }
}