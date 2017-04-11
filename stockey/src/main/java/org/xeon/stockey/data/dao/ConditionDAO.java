package org.xeon.stockey.data.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.po.ConditionPO;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.util.OperationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by nians on 2016/6/12.
 */
public class ConditionDAO {
    Session session;
    public ConditionDAO(){
        this.session = MySessionFactory.getSessionFactory().openSession();
    }

    public OperationResult addCondition(ConditionPO conditionPO) {
        if(findCondition(conditionPO.getUserID(),conditionPO.getName())!=null)
            return new OperationResult(false,"condition with this name already exists!");
        else{
            session.beginTransaction();
            session.save(conditionPO);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }

    public OperationResult deleteCondition(String userID, String name) {
        if(findCondition(userID,name)==null)
            return new OperationResult(false,"this condition doesn't exist!");
        else{
            session.beginTransaction();
            ConditionPO tmp = new ConditionPO();
            tmp.setUserID(userID);
            tmp.setName(name);
            session.clear();
            session.delete(tmp);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }

    public OperationResult<ConditionPO> updateCondition(ConditionPO conditionPO) {
        if(findCondition(conditionPO.getUserID(),conditionPO.getName())==null)
            return new OperationResult(false,"this condition doesn't exist!");
        else{
            session.beginTransaction();
            session.clear();
            session.update(conditionPO);
            session.getTransaction().commit();
            return new OperationResult<>(true,"success");
        }
    }

    public ConditionPO findCondition(String userID, String name) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(ConditionPO.class);
        cri.add(Restrictions.eq("userID", userID));
        cri.add(Restrictions.eq("name", name));
        if(cri.list().isEmpty())
            return null;
        else
            return (ConditionPO) cri.list().get(0);
    }

    public Iterator<ConditionPO> findAllCondition() {
        session.beginTransaction();
        Criteria cri = session.createCriteria(ConditionPO.class);
        if(cri.list().isEmpty())
            return null;
        else
            return (Iterator<ConditionPO>) cri.list().iterator();
    }

    public Iterator<ConditionPO> findConditionByUser(String userID) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(ConditionPO.class);
        cri.add(Restrictions.eq("userID", userID));
        if(cri.list().isEmpty())
            return null;
        else
            return (Iterator<ConditionPO>) cri.list().iterator();
    }

    public Iterator<ConditionPO> findConditionByName(String userID) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(ConditionPO.class);
        cri.add(Restrictions.eq("name", userID));
        if(cri.list().isEmpty())
            return null;
        else
            return (Iterator<ConditionPO>) cri.list().iterator();
    }

}
