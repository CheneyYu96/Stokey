package org.xeon.stockey.vo;

/**
 * 用来包装ConditionVO，来形成and, or, not等条件
 * Created by Sissel on 2016/6/16.
 */
public class ConditionWrapper
{
    public enum Type
    {
        AND,    // 表示 condition1 and condition2
        OR,     // 表示 condition1 or condition2
        NOT,    // 表示 not condition1
        NONE    // 表示 condition1
    }

    public Type type;
    public ConditionVO condition1;
    public ConditionVO condition2;
}
