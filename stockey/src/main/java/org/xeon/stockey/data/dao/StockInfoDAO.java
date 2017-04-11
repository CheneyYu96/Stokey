package org.xeon.stockey.data.dao;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.dataService.StockDataService;
import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StockInfoPO;
import org.xeon.stockey.util.OperationResult;

public class StockInfoDAO {
    Session session;

    public StockInfoDAO() {
        this.session = MySessionFactory.getSessionFactory().openSession();
    }

    public OperationResult addStockInfo(StockInfoPO stockInfo) {
//		if(findStockInfo(stockInfo.getSymbol())!=null)
//			return new OperationResult(false,"this stockInfo already exists!");
//		else{
        session.beginTransaction();
        session.save(stockInfo);
        session.getTransaction().commit();
        return new OperationResult(true, "success");
//		}

    }

    public OperationResult deleteStockInfo(String stockInfoID) {
        if (findStockInfo(stockInfoID) == null)
            return new OperationResult(false, "this stockInfo doesn't exist!");
        else {
            session.beginTransaction();
            StockInfoPO tmp = new StockInfoPO();
            tmp.setSymbol(stockInfoID);
            session.clear();
            session.delete(tmp);
            session.getTransaction().commit();
            return new OperationResult(true, "success");
        }
    }

    public OperationResult updateStockInfo(StockInfoPO stockInfo) {
        if (findStockInfo(stockInfo.getSymbol()) == null)
            return new OperationResult(false, "this stockInfo doesn't exist!");
        else {
            session.beginTransaction();
            session.clear();
            session.update(stockInfo);
            session.getTransaction().commit();
            return new OperationResult(true, "success");
        }
    }

    public StockInfoPO findStockInfo(String stockInfoID) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(StockInfoPO.class);
        cri.add(Restrictions.eq("symbol", stockInfoID));
        if (cri.list().isEmpty())
            return null;
        else
            return (StockInfoPO) cri.list().get(0);
    }

    @SuppressWarnings("unchecked")
    public Iterator<StockInfoPO> getAllStock(String userID) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(StockInfoPO.class);
        return (Iterator<StockInfoPO>) cri.list().iterator();
    }

    public Iterator<StockInfoPO> conditionSearch(Iterator<Filter> it) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(StockInfoPO.class);
        while (it.hasNext()) {
            Filter filter = it.next();
            switch (filter.compareType) {
                case LET:
                    cri.add(Restrictions.le(filter.fieldType.toString(), filter.value));
                    break;
                case LT:
                    cri.add(Restrictions.lt(filter.fieldType.toString(), filter.value));
                    break;
                case BET:
                    cri.add(Restrictions.ge(filter.fieldType.toString(), filter.value));
                    break;
                case BT:
                    cri.add(Restrictions.gt(filter.fieldType.toString(), filter.value));
                    break;
                case EQ:
                    cri.add(Restrictions.eq(filter.fieldType.toString(), filter.value));
                    break;
                case START_WITH:
                    cri.add(Restrictions.ilike(filter.fieldType.toString(), filter.value.toString(), MatchMode.START));
            }
        }
        return (Iterator<StockInfoPO>) cri.list().iterator();
    }

    public static void main(String[] args) throws NetworkConnectionException {
        StockDataService data = DataServiceFactory.getStockDataService();
    }


}
