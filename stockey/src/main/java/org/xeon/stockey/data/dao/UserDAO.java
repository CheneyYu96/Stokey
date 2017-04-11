package org.xeon.stockey.data.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.UserPO;

import java.util.Iterator;

/**
 * Created by nians on 2016/6/2.
 */
public class UserDAO {
    Session session;

    public UserDAO(){
        this.session = MySessionFactory.getSessionFactory().openSession();
    }

    public OperationResult addUser(UserPO user){
        if(
        user.getStoreID()!=null
        &&(
          findUserByStoreID(user.getStoreID())!=null
          ||findUserByEmail(user.getEmail())!=null
          ||findUserByUserID(user.getUserID())!=null
          )
        )
            return new OperationResult(false,"this User already exists!");
        else{
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }

    }
    public OperationResult deleteUser(String StoreID){
        if(findUserByStoreID(StoreID)==null)
            return new OperationResult(false,"this User doesn't exist!");
        else{
            session.beginTransaction();
            UserPO tmp = new UserPO();
            tmp.setStoreID(StoreID);
            session.clear();
            session.delete(tmp);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }
    public OperationResult updateUser(UserPO user){
        if(findUserByStoreID(user.getStoreID())==null)
            return new OperationResult(false,"this User doesn't exist!");
        else{
            session.beginTransaction();
            session.clear();
            session.update(user);
            session.getTransaction().commit();
            return new OperationResult(true,"success");
        }
    }

    public UserPO findUserByStoreID(String StoreID) {
        //session.beginTransaction();
        Criteria cri = session.createCriteria(UserPO.class);
        cri.add(Restrictions.eq("storeID", Integer.parseInt(StoreID)));
        if(cri.list().isEmpty())
            return null;
        else
            return ((UserPO) cri.list().get(0)).clone();
    }

    public UserPO findUserByUserID(String userID) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(UserPO.class);
        cri.add(Restrictions.eq("userID", userID));
        session.flush();
        session.clear();
        if(cri.list().isEmpty())
            return null;
        else
            return ((UserPO) cri.list().get(0)).clone();
    }

    public UserPO findUserByEmail(String email){
        session.beginTransaction();
        Criteria cri = session.createCriteria(UserPO.class);
        cri.add(Restrictions.eq("email", email));
        if(cri.list().isEmpty())
            return null;
        else
            return ((UserPO) cri.list().get(0)).clone();
    }

    public static void main(String[] args) {
        MySessionFactory sessionFactory = new MySessionFactory();
        UserDAO userDAO = new UserDAO();
        System.err.println("********************************");
        System.err.println("********************************");
        System.err.println("********************************");
        System.err.println(userDAO.addUser(new UserPO("niansong1996@163.com","niansong1996" , "12345")).reason);
        System.err.println("********************************");
        System.err.println("********************************");
        System.err.println("********************************");
    }
}
