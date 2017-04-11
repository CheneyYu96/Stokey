package org.xeon.stockey.vo;

import org.xeon.stockey.businessLogic.utility.WithConActFields;
import org.xeon.stockey.po.ConditionPO;

/**
 * 条件定义的VO
 * Created by Sissel on 2016/6/16.
 */
public class ConditionVO implements WithConActFields
{
    public String storeID;
    public String name;
    public String description;
    public String definition;

    public ConditionVO(String storeID, String name, String description, String definition)
    {
        this.storeID = storeID;
        this.name = name;
        this.description = description;
        this.definition = definition;
    }

    public ConditionVO(ConditionPO conditionPO)
    {
        this.storeID = conditionPO.getUserID();
        this.name = conditionPO.getName();
        this.description = conditionPO.getDescription();
        this.definition = conditionPO.getDefinition();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getStoreID()
    {
        return storeID;
    }

    @Override
    public String getDefinition()
    {
        return definition;
    }

    @Override
    public String getDescription()
    {
        return description;
    }
}

