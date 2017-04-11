package org.xeon.stockey.businessLogic.stockAnalysis;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算MA值
 * Created by Sissel on 2016/4/10.
 */
class MACalculator
{
    private static void fillWithNaN(Collection<Double> collection, int times)
    {
        for (int i = 0; i < times; i++)
        {
            collection.add(Double.NaN);
        }
    }

    /**
     * 计算ma
     * @param source 预先排序过的值的列表
     * @param interval 计算MA-N里面的N
     * @return 若前面的数据不足，就直接NaN，每个结果和原来的列表一一对应
     */
    public static ArrayList<Double> ma(List<Double> source, final int interval)
    {
        ArrayList<Double> copy = new ArrayList<>(source.size());
        ArrayList<Double> result = new ArrayList<>(source.size());
        copy.addAll(source);
        Collections.reverse(copy);

        Iterator<Double> beginRIter = copy.iterator();
        Iterator<Double> endRIter = copy.iterator();

        try
        {
            double sum = 0;
            int count = interval;
            while (count != 0)
            {
                --count;
                sum += beginRIter.next();
            }
            result.add(sum / interval);
            // now beginRIter points to the last nth one, endRIter points to the one next to the last one

            while (beginRIter.hasNext())
            {
                sum -= endRIter.next();
                sum += beginRIter.next();
                result.add(sum / interval);
            }
            MACalculator.fillWithNaN(result, source.size() - result.size());
            Collections.reverse(result);
        }
        catch (NoSuchElementException  e)
        {
            MACalculator.fillWithNaN(result, source.size());
        }

        return result;
    }

    private static double nextEMA(double beforeEMA, double currentValue, int interval)
    {
        double dInt = interval;
        return  2 / (dInt + 1) * currentValue + (dInt - 1) / (dInt + 1) * beforeEMA;
    }

    /**
     * 计算ema，只计算能看到的（第一项直接为收盘价），不管更前面的
     * @param source 预先排序过的值的列表
     * @param interval 计算EMA-N里面的N
     * @return 每个结果和原来的列表一一对应
     */
    public static ArrayList<Double> ema(List<Double> source, final int interval)
    {
        Iterator<Double> iterator = source.iterator();
        ArrayList<Double> result = new ArrayList<>(source.size());

        double before = iterator.next();
        result.add(before);
        while (iterator.hasNext())
        {
            double current = nextEMA(before, iterator.next(), interval);
            result.add(current);
            before = current;
        }

        return result;
    }
}
