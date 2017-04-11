package org.xeon.stockey.po;

/**
 * Created by nians on 2016/5/28.
 */
public class StrategyPO {

    public String strategyID;
    private String userStoreID;
    private String content;
    private String name;

    public StrategyPO(){};

    public StrategyPO(String userID, String content, String name) {
        this.userStoreID = null;
        this.content = content;
        this.name = name;
    }

    public String getStrategyID() {
        return strategyID;
    }

    public void setStrategyID(String strategyID) {
        this.strategyID = strategyID;
    }

    public String getUserStoreID() {
        return userStoreID;
    }

    public void setUserStoreID(String userStoreID) {
        this.userStoreID = userStoreID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String toStringID(int id) {
        String tmp = this.userStoreID +"";
        while(tmp.length()!=6){
            tmp = "0"+tmp;
        }
        return tmp;
    }
}
