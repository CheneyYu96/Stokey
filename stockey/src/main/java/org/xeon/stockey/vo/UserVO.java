package org.xeon.stockey.vo;

import org.xeon.stockey.po.StrategyPO;
import org.xeon.stockey.po.UserPO;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * VO of user information
 * Created by Sissel on 2016/5/30.
 */
public class UserVO
{
    /**
     * the email of the user
     */
    public String email;

    /**
     * the id of the user
     */
    public String userID;

    /**
     * the stockCodes selected by the user
     */
    public Collection<String> selections;

    /**
     * the strategy_ids created by the user
     */
    public Collection<String> strategies;

    /**
     * whether the email has been verified
     */
    public boolean isEmailVerified;

    /**
     * the internal identifier
     */
    public String storeID;

    /*
     * usually used as the first initialization of a user
     */
    public UserVO(String userID, String email)
    {
        this.userID = userID;
        this.email = email;
        this.selections = new LinkedList<>();
        this.strategies = new LinkedList<>();
        this.isEmailVerified = false;
        this.storeID = null;
    }

    /*
     * usually used when you need to change something, or created from a PO
     */
    public UserVO(String storeID , String email, String userID, Collection<String> selections, Collection<String> strategies, boolean isEmailVerified)
    {
        this.storeID = storeID;
        this.email = email;
        this.userID = userID;
        this.selections = selections;
        this.strategies = strategies;
        this.isEmailVerified = isEmailVerified;
    }

    /*
     * usually from data layer
     */
    public UserVO(UserPO userPO)
    {
        this.email = userPO.getEmail();
        this.userID = userPO.getUserID();
        this.selections = userPO.getSelections();
        this.strategies = userPO.getStrategies()
                .stream()
                .map(StrategyPO::getStrategyID)
                .collect(Collectors.toList());
        this.isEmailVerified = userPO.getIsEmailVerified();
        this.storeID = userPO.getStoreID();
    }
}