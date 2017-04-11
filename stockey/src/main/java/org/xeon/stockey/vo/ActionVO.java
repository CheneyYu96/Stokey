package org.xeon.stockey.vo;

import org.xeon.stockey.businessLogic.utility.WithConActFields;
import org.xeon.stockey.po.ActionPO;

/**
 * 动作定义的VO
 * Created by Sissel on 2016/6/16.
 */
public class ActionVO implements WithConActFields
{
    public String storeID;
    public String name;
    public String description;
    public String definition;

    public ActionVO(String storeID, String name, String description, String definition)
    {
        this.storeID = storeID;
        this.name = name;
        this.description = description;
        this.definition = definition;
    }

    public ActionVO(ActionPO actionPO)
    {
        this.storeID = actionPO.getUserID();
        this.name = actionPO.getName();
        this.description = actionPO.getDescription();
        this.definition = actionPO.getDefinition();
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
