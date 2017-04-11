package org.xeon.stockey.util;

import java.util.Collection;

/**
 * 更多集合操作
 * Created by Sissel on 2016/4/13.
 */
public class CollectionHelper
{
    public static <E> E getLastOne(Collection<E> collection)
    {
        E last = null;
        for (E e : collection)
        {
            last = e;
        }

        return last;
    }
}
