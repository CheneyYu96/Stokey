package org.xeon.stockey.businessLogic.stockAnalysis;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * 计算MA和EMA
 * Created by Sissel on 2016/4/10.
 */
public class MACalculatorTest
{
    @Test
    public void testMa() throws Exception
    {
        double[] doubles = new double[]{1, 2, 3, 4, 5, 6, 7};
        List<Double> list = new ArrayList<>();
        for (double aDouble : doubles)
        {
            list.add(aDouble);
        }

        Collection<Double> result = MACalculator.ma(list, 2);
        for (Double aDouble : result)
        {
            System.out.print(aDouble + " ");
        }
    }

    @Test
    public void testEma() throws Exception
    {
        double[] doubles = new double[]{9.20, 9.02, 8.89, 9.10, 9.65};
        double[] doubles26 = new double[]{9.43, 9.02, 8.89, 9.10, 9.65 };

        List<Double> list = new ArrayList<>();
        List<Double> list26 = new ArrayList<>();
        for (double aDouble : doubles)
        {
            list.add(aDouble);
        }
        for (Double aDouble : doubles26)
        {
            list26.add(aDouble);
        }

        ArrayList<Double> result = MACalculator.ema(list, 12);
        for (Double aDouble : result)
        {
            System.out.print(aDouble + " ");
        }
        System.out.println();

        ArrayList<Double> result26 = MACalculator.ema(list26, 26);
        for (Double aDouble : result26)
        {
            System.out.print(aDouble + " ");
        }
        System.out.println();

        for (int i = 0; i < result.size(); i++)
        {
            System.out.print((result.get(i) - result26.get(i)) + " ");
        }
    }
}