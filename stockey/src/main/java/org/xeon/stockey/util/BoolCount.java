package org.xeon.stockey.util;

/**
 *
 * Created by Sissel on 2016/5/12.
 */
public class BoolCount
{
    public static int count(Boolean...bools)
    {
        int sum = 0;
        for (Boolean bool : bools)
        {
            if (bool)
            {
                ++sum;
            }
        }

        return sum;
    }
}
