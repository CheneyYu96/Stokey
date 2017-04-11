package org.xeon.stockey.businessLogic.stockAnalysis;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

import static org.junit.Assert.*;

/**
 * 关于计算方差和协方差的测试
 * Created by Sissel on 2016/4/13.
 */
public class VarianceCalculatorTest
{
    @Test
    public void testCalculateVariance() throws Exception
    {
        List<Double> testCase = new ArrayList<>();

        Double[] sample1 = new Double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        testCase.clear();
        Collections.addAll(testCase, sample1);
        double result1 = VarianceCalculator.calculateVariance(testCase);
        System.out.println("expected: 2, actual: " + result1);
        Assert.assertEquals(2, result1, 0.1);

        Double[] sample2 = new Double[]{1.0, -1.0, 2.0, -2.0, 3.0, -3.0};
        testCase.clear();
        Collections.addAll(testCase, sample2);
        double result2 = VarianceCalculator.calculateVariance(testCase);
        System.out.println("expected: 4.666667, actual: " + result2);
        Assert.assertEquals(4.666667, result2, 0.1);

        Double[] sample3 = new Double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        testCase.clear();
        Collections.addAll(testCase, sample3);
        double result3 = VarianceCalculator.calculateVariance(testCase);
        System.out.println("expected: 0, actual: " + result3);
        Assert.assertEquals(0, result3, 0.1);
    }

    @Test
    public void testCalculateCovariance() throws Exception
    {
        List<Double> xCase = new ArrayList<>();
        List<Double> yCase = new ArrayList<>();

        Double[] sample11 = new Double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        Double[] sample12 = new Double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        xCase.clear();
        yCase.clear();
        Collections.addAll(xCase, sample11);
        Collections.addAll(yCase, sample12);
        double result1 = VarianceCalculator.calculateCovariance(xCase, yCase);
        System.out.println("expected: 2, actual: " + result1);
        Assert.assertEquals(2, result1, 0.1);

        Double[] sample21 = new Double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        Double[] sample22 = new Double[]{5.0, 4.0, 3.0, 2.0, 1.0};
        xCase.clear();
        yCase.clear();
        Collections.addAll(xCase, sample21);
        Collections.addAll(yCase, sample22);
        double result2 = VarianceCalculator.calculateCovariance(xCase, yCase);
        System.out.println("expected: -2, actual: " + result2);
        Assert.assertEquals(-2, result2, 0.1);

        Double[] sample31 = new Double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        Double[] sample32 = new Double[]{5.0, 4.0, 3.0, 2.0, 1.0};
        xCase.clear();
        yCase.clear();
        Collections.addAll(xCase, sample31);
        Collections.addAll(yCase, sample32);
        double result3 = VarianceCalculator.calculateCovariance(xCase, yCase);
        System.out.println("expected: 0, actual: " + result3);
        Assert.assertEquals(0, result3, 0.1);
    }
}