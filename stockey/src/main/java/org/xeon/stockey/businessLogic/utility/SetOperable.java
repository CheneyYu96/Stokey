package org.xeon.stockey.businessLogic.utility;

/**
 * Created by Sissel on 2016/3/5.
 * 继承该接口意味着这个类提供类似集合的交并差等操作
 */
public interface SetOperable<T extends SetOperable<T>>
{
    /**
     * 求两个集合的交集
     * @param another 另一个被操作的集合
     * @return 两个集合的交集
     */
    public T join(T another);

    /**
     * 求两个集合的并集
     * @param another 另一个被操作的集合
     * @return 两个集合的并集
     */
    public T union(T another);

    /**
     * 求该集合减去另一个集合的新集合
     * @param another 另一个被操作的集合
     * @return 该集合减去另一个集合的新集合
     */
    public T subtract(T another);
}
