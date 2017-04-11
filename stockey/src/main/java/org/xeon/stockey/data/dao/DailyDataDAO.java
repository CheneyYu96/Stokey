package org.xeon.stockey.data.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.xeon.stockey.businessLogic.utility.Filter;
import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.po.DailyDataPO;

public class DailyDataDAO {
	Session session;
	public DailyDataDAO(){
		this.session = MySessionFactory.getSessionFactory().openSession();
	}

	public OperationResult addDailyData(DailyDataPO dailyData){
		if(findDailyData(dailyData.getId())!=null)
			return new OperationResult(false,"this dailyData already exists!");
		else{
			session.beginTransaction();
			session.save(dailyData);
			session.getTransaction().commit();
			return new OperationResult(true,"success");
		}
	}

    public OperationResult addDailyDataBat(Iterator<DailyDataPO> it) {
        while(it.hasNext()){
            DailyDataPO po = it.next();
            session.beginTransaction();
            session.save(po);
        }
        session.getTransaction().commit();
        return new OperationResult(true,"success");
    }

    public OperationResult deleteDailyData(String dailyDataID){
		if(findDailyData(dailyDataID)==null)
			return new OperationResult(false,"this dailyData doesn't exist!");
		else{
			session.beginTransaction();
			DailyDataPO tmp = new DailyDataPO();
			tmp.setId(dailyDataID);
			session.clear();
			session.delete(tmp);
			session.getTransaction().commit();
			return new OperationResult(true,"success");
		}
	}
	public OperationResult updateDailyData(DailyDataPO dailyData){
		if(findDailyData(dailyData.getId())==null)
			return new OperationResult(false,"this dailyData doesn't exist!");
		else{
			session.beginTransaction();
			session.clear();
			session.update(dailyData);
			session.getTransaction().commit();
			return new OperationResult(true,"success");
		}
	}
	public DailyDataPO findDailyData(String dailyDataID){
		session.beginTransaction();
		Criteria cri = session.createCriteria(DailyDataPO.class);
		cri.add(Restrictions.eq("id", dailyDataID));
		if(cri.list().isEmpty())
			return null;
		else
			return (DailyDataPO) cri.list().get(0);
	}

    public Iterator<DailyDataPO> findDailyData(String stockCode, Calendar startDate, Calendar endDate) {
        Session session = MySessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria cri = session.createCriteria(DailyDataPO.class);
        cri.add(Restrictions.ilike("id", stockCode, MatchMode.START));
        cri.add(Restrictions.between("theDate",startDate,endDate));
        List list = cri.list();
        if(list.isEmpty())
            return null;
        else
            return (Iterator<DailyDataPO>) list.iterator();
    }

	private Calendar getLatest(){
		session.beginTransaction();
		Criteria cri = session.createCriteria(DailyDataPO.class);
		cri.setProjection( Projections.projectionList().add(Projections.max("theDate" ) ) ).uniqueResult() ;
		if(cri.list().isEmpty())
			return null;
		else
			return (Calendar) cri.list().get(0);
	}

    public Calendar getLatestForStock(String stockCode) {
        return UtilityTools.String2Cal("2016-06-17");
//        session.beginTransaction();
//        Criteria cri = session.createCriteria(DailyDataPO.class);
//        cri.add(Restrictions.like("id",stockCode.substring(2), MatchMode.START));
//        cri.setProjection( Projections.projectionList().add(Projections.max("theDate" ) ) ).uniqueResult() ;
//        if(cri.list().isEmpty())
//            return null;
//        else
//            return (Calendar) cri.list().get(0);
    }

    public Iterator<DailyDataPO> conditionSearch(Iterator<Filter> it) {
        session.beginTransaction();
        Criteria cri = session.createCriteria(DailyDataPO.class);
        while (it.hasNext()) {
            Filter filter = it.next();
            switch (filter.compareType) {
                case LET: cri.add(Restrictions.le(filter.fieldType.toString(),filter.value));break;
                case LT: cri.add(Restrictions.lt(filter.fieldType.toString(),filter.value));break;
                case BET: cri.add(Restrictions.ge(filter.fieldType.toString(),filter.value));break;
                case BT: cri.add(Restrictions.gt(filter.fieldType.toString(),filter.value));break;
                case EQ: cri.add(Restrictions.eq(filter.fieldType.toString(),filter.value));break;
                case START_WITH:
                    cri.add(Restrictions.ilike(filter.fieldType.toString(), filter.value.toString(), MatchMode.START));
            }
        }
        return (Iterator<DailyDataPO>) cri.list().iterator();
    }

    public static void main1(String[] args) {
        MySessionFactory sessionFactory = new MySessionFactory();
        DailyDataDAO dailyDataDAO = new DailyDataDAO();
        Calendar c = dailyDataDAO.getLatestForStock("sh600036");
        System.out.println(UtilityTools.Cal2String(c));
    }

	public static void main(String[] args){
		MySessionFactory sessionFactory = new MySessionFactory();
		DailyDataDAO dao = new DailyDataDAO();
        Calendar startDate = UtilityTools.String2Cal("2016-01-04");
        Calendar endDate = UtilityTools.String2Cal("2016-01-08");

        System.out.println(UtilityTools.Cal2String(startDate));
        System.out.println(UtilityTools.Cal2String(endDate));

        System.err.println("begin " + startDate.get(Calendar.MONTH) + " " + startDate.get(Calendar.DAY_OF_MONTH));
        System.err.println("end " + endDate.get(Calendar.DAY_OF_MONTH));

        ArrayList<Filter> arrayList = new ArrayList<>();
        Filter filter = new Filter(Filter.FieldType.id,DailyDataPO.class, Filter.CompareType.START_WITH,"601398");
        Filter filter1 = new Filter(Filter.FieldType.theDate, DailyDataPO.class, Filter.CompareType.BET, startDate);
        Filter filter2 = new Filter(Filter.FieldType.theDate, DailyDataPO.class, Filter.CompareType.LET, endDate);
        arrayList.add(filter);
        arrayList.add(filter1);
        arrayList.add(filter2);
        Iterator<DailyDataPO> it = dao.conditionSearch(arrayList.iterator());
        while (it.hasNext()) {
            it.next().print();
        }
    }
}
