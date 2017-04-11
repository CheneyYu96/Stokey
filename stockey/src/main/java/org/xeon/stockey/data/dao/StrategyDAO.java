package org.xeon.stockey.data.dao;

import com.sun.org.apache.regexp.internal.RE;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.StrategyPO;
import org.xeon.stockey.util.OperationResult;

import java.util.Iterator;

/**
 * Created by nians on 2016/6/2.
 */
public class StrategyDAO {
    Session session;

    public StrategyDAO(){
        this.session = MySessionFactory.getSessionFactory().openSession();
    }

    public OperationResult addStrategy(StrategyPO strategy){
        if(findStrategy(strategy.getStrategyID())!=null)
            return new OperationResult(false,"this Strategy already exists!");
        else{
            session.beginTransaction();
            session.save(strategy);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }

    }
    public OperationResult deleteStrategy(String strategyID){
        if(findStrategy(strategyID)==null)
            return new OperationResult(false,"this Strategy doesn't exist!");
        else{
            session.beginTransaction();
            StrategyPO tmp = new StrategyPO();
            tmp.setUserStoreID(strategyID);
            session.clear();
            session.delete(tmp);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }
    public OperationResult updateStrategy(StrategyPO strategy){
        if(findStrategy(strategy.getStrategyID())==null)
            return new OperationResult(false,"this Strategy doesn't exist!");
        else{
            session.beginTransaction();
            session.clear();
            session.update(strategy);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }

    public StrategyPO findStrategy(String strategyID){
        session.beginTransaction();
        Criteria cri = session.createCriteria(StrategyPO.class);
        cri.add(Restrictions.eq("strategyID",strategyID));
        if(cri.list().isEmpty())
            return null;
        else
            return (StrategyPO) cri.list().get(0);
    }

    public StrategyPO findStrategy(String userID, String name){
        session.beginTransaction();
        Criteria cri = session.createCriteria(StrategyPO.class);
        cri.add(Restrictions.eq("userStoreID",userID));
        cri.add(Restrictions.eq("name",name));
        if(cri.list().isEmpty())
            return null;
        else
            return (StrategyPO) cri.list().get(0);
    }

    public Iterator<StrategyPO> findOneUserStrategy(String userStoreID) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(StrategyPO.class);
        cri.add(Restrictions.eq("userStoreID",userStoreID));
        return (Iterator<StrategyPO>) cri.list().iterator();
    }
}
