package org.xeon.stockey.businessLogic.stockAnalysis;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;

/**
 * 帮助计算方差
 * Created by Sissel on 2016/4/13.
 */
public class VarianceCalculator
{
    private static double mean(Collection<Double> values)
    {
        double sum = 0;
        for (Double value : values)
        {
            sum += value;
        }

        return sum / values.size();
    }

    public static double calculateVariance(Collection<Double> values)
    {
        double mean = mean(values);

        double sumOfDiffSqr = 0;
        for (Double value : values)
        {
            sumOfDiffSqr += Math.pow(value - mean, 2);
        }

        return sumOfDiffSqr / values.size();
    }

    public static double calculateCovariance(Collection<Double> xs, Collection<Double> ys)
    {
        double xAvg = mean(xs);
        double yAvg = mean(ys);

        double sum = 0;
        int count = 0;
        Iterator<Double> xIter = xs.iterator();
        Iterator<Double> yIter = ys.iterator();

        while (xIter.hasNext() && yIter.hasNext())
        {
            sum += (xIter.next() - xAvg) * (yIter.next() - yAvg);
            ++count;
        }

        return sum / count;
    }

    public static double calculateCorrelationCoefficient(Collection<Double> xs, Collection<Double> ys)
    {
        double varX = calculateVariance(xs);
        double varY = calculateVariance(ys);
        double covar = calculateCovariance(xs, ys);

        return covar / (Math.sqrt(varX) * Math.sqrt(varY));
    }
}
