package org.xeon.stockey.businessLogic.stockAnalysis;

import org.junit.Assert;
import org.junit.Test;
import org.xeon.stockey.util.ListPrintHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * RSI计算的测试
 * Created by Sissel on 2016/4/13.
 */
public class RSICalculatorTest
{
    @Test
    public void testCalculateRsi() throws Exception
    {
        Double[] sample = new Double[]{23.7, 27.9, 26.5, 29.6, 31.1, 29.4, 25.5, 28.9, 20.5, 23.2};
        double[] expected1 = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 49.41};
        List<Double> testCase1 = new ArrayList<>(10);
        Collections.addAll(testCase1, sample);
        ArrayList<Double> result1 = RSICalculator.calculateRsi(testCase1, 9);
        System.out.print("expected: ");
        ListPrintHelper.printDoubles(expected1);
        System.out.print("actual: ");
        ListPrintHelper.printDoubles(result1);
        System.out.println();
        for (int i = 0; i < expected1.length; i++)
        {
            Assert.assertEquals(expected1[i], result1.get(i), 1);
        }
    }
}