package org.xeon.stockey.po;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by nians on 2016/5/28.
 */
public class UserPO {
    private int storeID;
    private String userID;
    private String email;
    private String  password;
    private boolean isEmailVerified;

    private ArrayList<StrategyPO> strategies;
    private ArrayList<String> selections;

    public UserPO(){
        this.selections = new ArrayList<>();
        this.strategies = new ArrayList<>();
    }

    public UserPO(UserPO po) {
        this.selections = new ArrayList<>();
        this.strategies = new ArrayList<>();

        for (String selection : po.selections) {
            this.selections.add(selection);
        }
        for (StrategyPO strategy : po.strategies) {
            this.strategies.add(strategy);
        }

        this.storeID = po.storeID;
        this.userID = po.userID;
        this.email = po.email;
        this.password = po.password;
        this.isEmailVerified = po.isEmailVerified;
    }

    public UserPO(String email,String userID, String password) {
        this.email = email;
        this.userID = userID;
        this.password = password;
        this.isEmailVerified = false;
        this.strategies = new ArrayList<>();
        this.selections = new ArrayList<>();
    }

    public String getStoreID() {
        return this.toStringID(storeID);
    }

    public void setStoreID(String storeID) {
        this.storeID = Integer.parseInt(storeID);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<StrategyPO> getStrategies() {
        return strategies;
    }


    public ArrayList<String> getSelections() {
        return selections;
    }


    public void addStrategy(StrategyPO strategyPO){
        this.strategies.add(strategyPO);
    }
    public void addSelection(String selection){
        this.selections.add(selection);
    }

    public boolean getIsEmailVerified()
    {
        return isEmailVerified;
    }

    public void setIsEmailVerified(boolean emailVerified)
    {
        isEmailVerified = emailVerified;
    }

    private String toStringID(int id) {
        String tmp = id+"";
        while(tmp.length()!=6){
            tmp = "0"+tmp;
        }
        return tmp;
    }

    public UserPO clone() {
        return new UserPO(this);
    }
}
