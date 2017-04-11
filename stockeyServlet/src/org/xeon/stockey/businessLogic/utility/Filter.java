package org.xeon.stockey.businessLogic.utility;

/**
 * Created by Sissel on 2016/3/5.
 * 过滤条件，用来简单表示大于小于等于某值等，目的是，抽象化过滤，让改变过滤的实现方式时前端不需要改变
 */
public class Filter
{
    /**
     * 表示要比较的是哪个域
     */
    public enum FieldType
    {
        stockCode,
        exchange,
        year,
        date,
        open,
        high,
        low,
        close,
        adj_price,
        volume,
        turnover,
        pe,
        pb
    }

    /**
     * 表示该过滤条件用哪种方式比较
     */
    public enum CompareType
    {
        LT, // actualValue < value
        LET, // actualValue <= value
        BT, // value < actualValue
        BET, // value <= actualValue
        EQ, // actualValue == equalValue
    }

    /**
     * 表示该过滤条件的数据是什么类型
     */
    public enum DataType
    {
        STRING, // convert to String
        NUM, // convert to double
        EXCHANGE_ENUM, // convert to ExchangeEnum
        TIME // convert to LocalDate
    }

    public FieldType fieldType; // 表示该过滤条件的域的名字
    public Object value;
    public CompareType compareType; // 表示该过滤条件用哪种方式比较
    public Class dataType; // 表示该过滤条件的数据是什么类型

    public Filter(FieldType fieldType, Class dataType, CompareType compareType, Object value)
    {
        this.fieldType = fieldType;
        this.compareType = compareType;
        this.dataType = dataType;
        this.value = value;
    }
}

