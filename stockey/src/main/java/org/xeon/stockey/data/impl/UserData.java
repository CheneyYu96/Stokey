package org.xeon.stockey.data.impl;

import org.xeon.stockey.data.dao.StrategyDAO;
import org.xeon.stockey.data.dao.UserDAO;
import org.xeon.stockey.data.dao.UserPreferenceDAO;
import org.xeon.stockey.dataService.UserDataService;
import org.xeon.stockey.po.StrategyPO;
import org.xeon.stockey.po.UserPO;
import org.xeon.stockey.po.UserPreferencePO;
import org.xeon.stockey.util.OperationResult;

import java.util.Iterator;

/**
 * Created by nians on 2016/6/2.
 */
public class UserData implements UserDataService{
    UserDAO userDAO;
    StrategyDAO strategyDAO;
    UserPreferenceDAO userPreferenceDAO;

    UserData() {
        this.userDAO = new UserDAO();
        this.strategyDAO = new StrategyDAO();
        this.userPreferenceDAO = new UserPreferenceDAO();
    }

    @Override
    public UserPO getUserByEmail(String email) {
        UserPO user = userDAO.findUserByEmail(email);
        if(user==null) return null;
        return this.assemblyUser(user);
    }

    @Override
    public UserPO getUserByUserID(String userID) {
        UserPO user = userDAO.findUserByUserID(userID);
        if(user==null) return null;
        return this.assemblyUser(user);
    }
    @Override
    public UserPO getUserByStoreID(String storeID) {
        UserPO user = userDAO.findUserByStoreID(storeID);
        if(user==null) return null;
        return this.assemblyUser(user);
    }

    @Override
    public OperationResult registerAccount(String email, String userID, String password) {
        UserPO user = new UserPO(email,userID,password);
        return userDAO.addUser(user);
    }

    @Override
    public OperationResult verifyEmail(String email) {
        UserPO user = userDAO.findUserByEmail(email);
        user.setIsEmailVerified(true);
        return userDAO.updateUser(user);
    }

    @Override
    public OperationResult modifyPassword(String storeID, String newPassword) {
        UserPO user = userDAO.findUserByStoreID(storeID);
        user.setPassword(newPassword);
        return userDAO.updateUser(user);
    }

    @Override
    public OperationResult<UserPO> addStrategy(StrategyPO newStrategy) {
        OperationResult<UserPO> subResult = strategyDAO.addStrategy(newStrategy);
        subResult.setBundle(this.getUserByStoreID(newStrategy.getUserStoreID()));
        return subResult;
    }

    @Override
    public OperationResult<UserPO> deleteStrategy(String strategyID) {
        StrategyPO strategyPO = strategyDAO.findStrategy(strategyID,null);
        OperationResult<UserPO> subResult = strategyDAO.deleteStrategy(strategyID);
        subResult.setBundle(this.getUserByStoreID(strategyPO.getUserStoreID()));
        return subResult;
    }

    @Override
    public OperationResult modifyStrategy(StrategyPO modifiedStrategy) {
        return strategyDAO.updateStrategy(modifiedStrategy);
    }

    @Override
    public OperationResult<UserPO> addSelection(String storeID, String stockCode) {
        UserPreferencePO userPreferencePO = new UserPreferencePO(storeID,stockCode);
        OperationResult<UserPO> subResult = userPreferenceDAO.addUserPreference(userPreferencePO);
        subResult.setBundle(this.getUserByStoreID(storeID));
        return subResult;
    }

    @Override
    public OperationResult<UserPO> deleteSelection(String storeID, String stockCode) {
        OperationResult<UserPO> subResult = userPreferenceDAO.deleteUserPreference(storeID,stockCode);
        subResult.setBundle(this.getUserByStoreID(storeID));
        return subResult;
    }

    private UserPO assemblyUser(UserPO userPO) {
        Iterator<StrategyPO> strategyPOIterator = strategyDAO.findOneUserStrategy(userPO.getStoreID());
        while (strategyPOIterator.hasNext()) {
            userPO.addStrategy(strategyPOIterator.next());
        }

        Iterator<UserPreferencePO> selectionIterator = userPreferenceDAO.findUserAllPreference(userPO.getStoreID());
        while (selectionIterator.hasNext()) {
            userPO.addSelection(selectionIterator.next().getStockCode());
        }

        return userPO;
    }

    public static void main(String[] args) {
        UserData data = new UserData();
        UserPO uesr = data.getUserByStoreID("000004");
        System.out.println(uesr.getSelections().size());
        UserPO user = data.getUserByStoreID("000004");
        System.out.println(user.getSelections().size());
    }
}
