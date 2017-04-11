package org.xeon.stockey.po;

import java.io.Serializable;

public class UserPreferencePO implements Serializable{
    private String userStoreID;
    private String stockCode;

    public UserPreferencePO() {};

    public UserPreferencePO(String userID, String stockCode) {
        this.userStoreID = userID;
        this.stockCode = stockCode;
    }

    public String getUserStoreID() {
        return userStoreID;
    }

    public void setUserStoreID(String userStoreID) {
        this.userStoreID = userStoreID;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    private String toStringID(int id) {
        String tmp = id+"";
        while(tmp.length()!=6){
            tmp = "0"+tmp;
        }
        return tmp;
    }
}
