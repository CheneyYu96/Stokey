package org.xeon.stockey.vo;

import org.xeon.stockey.po.StrategyPO;

/**
 * the vo of strategy
 * Created by Sissel on 2016/5/30.
 */
public class StrategyVO
{
    /**
     * the userID of the owner
     */
    public String ownerID;

    /**
     * the id of the strategy
     */
    public String strategyID;

    /**
     * the python code of this strategy
     */
    public String content;

    /**
     * the name of this strategy
     */
    public String name;

    public StrategyVO(String ownerID, String strategyID, String content, String name)
    {
        this.ownerID = ownerID;
        this.strategyID = strategyID;
        this.content = content;
        this.name = name;
    }

    public StrategyVO(StrategyPO strategyPO)
    {
        this.strategyID = strategyPO.getStrategyID();
        this.ownerID = strategyPO.getUserStoreID();
        this.name = strategyPO.getName();
        this.content = strategyPO.getContent();
    }

    public StrategyPO toStrategyPO()
    {
        StrategyPO result = new StrategyPO();
        result.setStrategyID(this.strategyID);
        result.setContent(this.content);
        result.setName(this.name);
        result.setUserStoreID(this.ownerID);
        return result;
    }
}
