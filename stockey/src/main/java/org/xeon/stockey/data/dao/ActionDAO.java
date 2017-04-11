package org.xeon.stockey.data.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.po.ActionPO;
import org.xeon.stockey.po.ConditionPO;
import org.xeon.stockey.util.OperationResult;

import java.util.Iterator;

/**
 * Created by nians on 2016/6/12.
 */
public class ActionDAO {
    Session session;
    public ActionDAO(){
        this.session = MySessionFactory.getSessionFactory().openSession();
    }

    public OperationResult addAction(ActionPO actionPO) {
        if(findAction(actionPO.getUserID(),actionPO.getName())!=null)
            return new OperationResult(false,"this action with this userID and name already exists!");
        else{
            session.beginTransaction();
            session.save(actionPO);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }

    public OperationResult deleteAction(String userID, String name) {
        if(findAction(userID,name)==null)
            return new OperationResult(false,"this action doesn't exist!");
        else{
            session.beginTransaction();
            ActionPO tmp = new ActionPO();
            tmp.setUserID(userID);
            tmp.setName(name);
            session.clear();
            session.delete(tmp);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }

    public OperationResult updateAction(ActionPO actionPO) {
        if(findAction(actionPO.getUserID(),actionPO.getName())==null)
            return new OperationResult(false,"this action doesn't exist!");
        else{
            session.beginTransaction();
            session.clear();
            session.update(actionPO);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }

    public ActionPO findAction(String userID, String name) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(ActionPO.class);
        cri.add(Restrictions.eq("userID", userID));
        cri.add(Restrictions.eq("name", name));
        if(cri.list().isEmpty())
            return null;
        else
            return (ActionPO) cri.list().get(0);
    }

    public Iterator<ActionPO> findActionByUser(String userID) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(ActionPO.class);
        cri.add(Restrictions.eq("userID", userID));
        if(cri.list().isEmpty())
            return null;
        else
            return (Iterator<ActionPO>) cri.list().iterator();
    }

    public Iterator<ActionPO> findActionByName(String userID) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(ActionPO.class);
        cri.add(Restrictions.eq("name", userID));
        if(cri.list().isEmpty())
            return null;
        else
            return (Iterator<ActionPO>) cri.list().iterator();
    }

    public Iterator<ActionPO> getAllAction() {
        session.beginTransaction();
        Criteria cri = session.createCriteria(ActionPO.class);
        if(cri.list().isEmpty())
            return null;
        else
            return (Iterator<ActionPO>) cri.list().iterator();
    }

}
