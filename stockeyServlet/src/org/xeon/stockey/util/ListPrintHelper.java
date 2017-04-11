package org.xeon.stockey.util;

import java.util.Collection;

/**
 * 方便输出打印
 * Created by Sissel on 2016/4/13.
 */
public class ListPrintHelper
{
    // the redundancy of these 2 methods should blame the stupid java wrapper
    public static void printDoubles(Collection<Double> doubles)
    {
        int count = 1;
        for (Double aDouble : doubles)
        {
            System.out.print(aDouble + ", ");
            if (count % 10 == 0)
            {
                System.out.println();
            }
            ++count;
        }
    }

    // the redundancy of these 2 methods should blame the stupid java wrapper
    public static void printDoubles(double[] expected1)
    {
        int count = 1;
        for (Double aDouble : expected1)
        {
            System.out.print(aDouble + ", ");
            if (count % 10 == 0)
            {
                System.out.println();
            }
            ++count;
        }
    }
}
