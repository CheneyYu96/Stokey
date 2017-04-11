package org.xeon.stockey.vo;

import java.util.List;

/**
 * 用来包装action，形成action的拼接，action嵌套Recipe
 * Created by Sissel on 2016/6/16.
 */
public class ActionWrapper
{
    public enum Type
    {
        LIST,    // 表示obj是一个ActionWrapper列表（表示一个又一个的拼接）
        ACTION,  // 表示obj是一个ActionVO
        RECIPE   // 表示obj是一个RECIPE
    }

    public Type type;
    private Object obj;

    public ActionWrapper(Type type, Object obj)
    {
        this.type = type;
        this.obj = obj;
    }

    public List<ActionWrapper> getList()
    {
        return (List<ActionWrapper>)obj;
    }

    public ActionVO getAction()
    {
        return (ActionVO)obj;
    }

    public RecipeVO getRecipe()
    {
        return (RecipeVO)obj;
    }
}
