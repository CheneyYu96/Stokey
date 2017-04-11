package org.xeon.stockey.vo;

/**
 * Recipe的VO
 * Created by Sissel on 2016/6/16.
 */
public class RecipeVO
{
    public ConditionWrapper conditionWrapper;
    public ActionWrapper actionWrapper;

    public RecipeVO(ConditionWrapper conditionWrapper, ActionWrapper actionWrapper)
    {
        this.conditionWrapper = conditionWrapper;
        this.actionWrapper = actionWrapper;
    }
}
