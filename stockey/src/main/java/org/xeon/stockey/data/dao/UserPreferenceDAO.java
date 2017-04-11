package org.xeon.stockey.data.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.po.UserPreferencePO;
import org.xeon.stockey.util.OperationResult;

import java.util.Iterator;

/**
 * Created by nians on 2016/6/2.
 */
public class UserPreferenceDAO {
    Session session;

    public UserPreferenceDAO(){
        this.session = MySessionFactory.getSessionFactory().openSession();
    }

    public OperationResult addUserPreference(UserPreferencePO UserPreference){
        if(findUserPreference(UserPreference.getUserStoreID(),UserPreference.getStockCode())!=null)
            return new OperationResult(false,"this UserPreference already exists!");
        else{
            session.beginTransaction();
            session.save(UserPreference);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }

    }
    public OperationResult deleteUserPreference(String userID, String stockCode){
        if(findUserPreference(userID,stockCode)==null)
            return new OperationResult(false,"this UserPreference doesn't exist!");
        else{
            session.beginTransaction();
            UserPreferencePO tmp = new UserPreferencePO();
            tmp.setUserStoreID(userID);
            tmp.setStockCode(stockCode);
            session.clear();
            session.delete(tmp);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }
    public OperationResult updateUserPreference(UserPreferencePO UserPreference){
        if(findUserPreference(UserPreference.getUserStoreID(),UserPreference.getStockCode())==null)
            return new OperationResult(false,"this UserPreference doesn't exist!");
        else{
            session.beginTransaction();
            session.clear();
            session.update(UserPreference);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }

    public UserPreferencePO findUserPreference(String userID, String stockCode) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(UserPreferencePO.class);
        cri.add(Restrictions.eq("userStoreID", userID));
        cri.add(Restrictions.eq("stockCode", stockCode));
        if(cri.list().isEmpty())
            return null;
        else
            return (UserPreferencePO) cri.list().get(0);
    }

    public Iterator<UserPreferencePO> findUserAllPreference(String userStoreID){
        session.beginTransaction();
        Criteria cri = session.createCriteria(UserPreferencePO.class);
        cri.add(Restrictions.eq("userStoreID", userStoreID));
        return (Iterator<UserPreferencePO>) cri.list().iterator();
    }

    public static void main(String[] args) {
        UserPreferenceDAO userPreferenceDAO = new UserPreferenceDAO();
        OperationResult result = userPreferenceDAO.deleteUserPreference("000004", "sh600606");
    }

}
