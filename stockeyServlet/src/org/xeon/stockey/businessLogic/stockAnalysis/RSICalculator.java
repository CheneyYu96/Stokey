package org.xeon.stockey.businessLogic.stockAnalysis;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 计算RSI用
 * Created by Sissel on 2016/4/13.
 */
public class RSICalculator
{
    public static ArrayList<Double> calculateRsi(Collection<Double> closes, int n)
    {
        double riseSum = 0;
        double descSum = 0;
        ArrayDeque<Double> queue = new ArrayDeque<>(n);
        ArrayList<Double> result = new ArrayList<>(closes.size());
        Iterator<Double> iterator = closes.iterator();
        double before = iterator.next();

        if (closes.size() < n)
        {
            for (int i = 0; i < n; i++)
            {
                result.add(0.0);
            }
            return result;
        }

        for (int i = 0; i < n; i++)
        {
            result.add(0.0); // when for ends, result has n 0.0
            double current = iterator.next();
            queue.offerLast(current - before);

            if(queue.getLast() <= 0)
            {
                descSum -= queue.getLast();
            }
            else
            {
                riseSum += queue.getLast();
            }

            before = current;
        }

        while (iterator.hasNext())
        {
            double riseAvg = riseSum / n;
            double descAvg = descSum / n;
            double rsi = (riseAvg / (riseAvg + descAvg)) * 100;
            result.add(rsi);

            double current = iterator.next();
            double diff = current - before;
            double first = queue.poll();
            if (first < 0)
            {
                descSum += first;
            }
            else
            {
                riseSum -= first;
            }

            queue.offerLast(diff);
            if (diff < 0)
            {
                descSum -= diff;
            }
            else
            {
                riseSum += diff;
            }

            before = current;
        }
        // one more time
        double riseAvg = riseSum / n;
        double descAvg = descSum / n;
        double rsi = (riseAvg / (riseAvg + descAvg)) * 100;
        result.add(rsi);

        return result;
    }
}
