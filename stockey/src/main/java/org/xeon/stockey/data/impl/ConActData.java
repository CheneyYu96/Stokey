package org.xeon.stockey.data.impl;

import org.xeon.stockey.data.dao.ActionDAO;
import org.xeon.stockey.data.dao.ConditionDAO;
import org.xeon.stockey.dataService.ConActDataService;
import org.xeon.stockey.po.ActionPO;
import org.xeon.stockey.po.ConditionPO;
import org.xeon.stockey.util.OperationResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by nians on 2016/6/17.
 */
public class ConActData implements ConActDataService {
    ConditionDAO conditionDAO;
    ActionDAO actionDAO;

    public ConActData() {
        conditionDAO = new ConditionDAO();
        actionDAO = new ActionDAO();
    }


    @Override
    public OperationResult addCondition(ConditionPO conditionPO) {
        return conditionDAO.addCondition(conditionPO);
    }

    @Override
    public OperationResult deleteCondition(String storeID, String conName) {
        return conditionDAO.deleteCondition(storeID,conName);
    }

    @Override
    public OperationResult<ConditionPO> updateCondition(ConditionPO conditionPO) {
        OperationResult<ConditionPO> operationResult = conditionDAO.updateCondition(conditionPO);
        operationResult.setBundle(conditionDAO.findCondition(conditionPO.getUserID(),conditionPO.getName()));
        return operationResult;
    }

    @Override
    public Collection<ConditionPO> searchConByKey(String key) {
        ArrayList<ConditionPO> result = new ArrayList();
        Iterator<ConditionPO> it = this.conditionDAO.findConditionByName(key);
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    @Override
    public Collection<ConditionPO> searchConByOwner(String storeID) {
        ArrayList<ConditionPO> result = new ArrayList();
        Iterator<ConditionPO> it = this.conditionDAO.findConditionByUser(storeID);
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    @Override
    public ConditionPO getCondition(String storeID, String conName) {
        return conditionDAO.findCondition(storeID,conName);
    }

    @Override
    public Iterator<ConditionPO> getRandomConditions(int count) {
        Iterator<ConditionPO> it = conditionDAO.findAllCondition();
        ArrayList<ConditionPO> allConditions = new ArrayList<>();
        while (it.hasNext()) {
            allConditions.add(it.next());
        }
        if (allConditions.size() <= count) {
            return allConditions.iterator();
        }

        ArrayList<ConditionPO> result = new ArrayList<>();
        for(int i=0;i<count;i++) {
            int index = (int) (allConditions.size()*Math.random());
            result.add(allConditions.get(index));
            allConditions.remove(index);
        }



        return result.iterator();
    }

    @Override
    public OperationResult addAction(ActionPO actionPO) {
        return actionDAO.addAction(actionPO);
    }

    @Override
    public OperationResult deleteAction(String storeID, String actName) {
        return actionDAO.deleteAction(storeID, actName);
    }

    @Override
    public OperationResult<ActionPO> updateAction(ActionPO actionPO) {
        OperationResult<ActionPO> operationResult = actionDAO.updateAction(actionPO);
        operationResult.setBundle(actionDAO.findAction(actionPO.getUserID(),actionPO.getName()));
        return operationResult;
    }

    @Override
    public Collection<ActionPO> searchActByKey(String key) {
        ArrayList<ActionPO> result = new ArrayList();
        Iterator<ActionPO> it = this.actionDAO.findActionByName(key);
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    @Override
    public Collection<ActionPO> searchActByOwner(String storeID) {
        ArrayList<ActionPO> result = new ArrayList();
        Iterator<ActionPO> it = this.actionDAO.findActionByUser(storeID);
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    @Override
    public ActionPO getAction(String storeID, String actName) {
        return actionDAO.findAction(storeID,actName);
    }

    @Override
    public Iterator<ActionPO> getRandomActions(int count) {
        Iterator<ActionPO> it = actionDAO.getAllAction();
        ArrayList<ActionPO> allActions = new ArrayList<>();
        while (it.hasNext()) {
            allActions.add(it.next());
        }
        if (allActions.size() <= count) {
            return allActions.iterator();
        }

        ArrayList<ActionPO> result = new ArrayList<>();
        for(int i=0;i<count;i++) {
            int index = (int) (allActions.size()*Math.random());
            result.add(allActions.get(index));
            allActions.remove(index);
        }



        return result.iterator();
    }
}