package org.xeon.stockey.businessLogic.strategy;

import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.businessLogicService.strategyService.ConActService;
import org.xeon.stockey.data.impl.ConActData;
import org.xeon.stockey.dataService.ConActDataService;
import org.xeon.stockey.po.ActionPO;
import org.xeon.stockey.po.ConditionPO;
import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.vo.ActionVO;
import org.xeon.stockey.vo.ConditionVO;
import sun.security.krb5.internal.APOptions;
import sun.security.x509.AVA;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * ConActService 的实现
 * Created by Sissel on 2016/6/18.
 */
public class ConActImpl implements ConActService
{
    ConActDataService dataService = new ConActData();

    @Override
    public OperationResult<ConditionVO> addCondition(String storeID, String conName, String description, String definition)
    {
        ConditionPO conditionPO = new ConditionPO(storeID, conName, description, definition);
        OperationResult dataResult = dataService.addCondition(conditionPO);

        OperationResult<ConditionVO> result = new OperationResult<>(dataResult);

        if (dataResult.success)
        {
            result.setBundle(new ConditionVO(conditionPO));
        }

        return result;
    }

    @Override
    public OperationResult deleteCondition(String storeID, String conName)
    {
        return dataService.deleteCondition(storeID, conName);
    }

    @Override
    public OperationResult<ConditionVO> updateCondition(String storeID, String conName, String description, String definition)
    {
        ConditionPO conditionPO = new ConditionPO(storeID, conName, description, definition);
        OperationResult dataResult = dataService.updateCondition(conditionPO);

        OperationResult<ConditionVO> result = new OperationResult<>(dataResult);

        if (dataResult.success)
        {
            result.setBundle(new ConditionVO(conditionPO));
        }

        return result;
    }

    private Collection<ConditionVO> transConsFromData(Collection<ConditionPO> dataResult)
    {
        if (dataResult == null)
        {
            return null;
        }
        else
        {
            return dataResult.stream().map(ConditionVO::new).collect(Collectors.toList());
        }
    }

    @Override
    public OperationResult<Collection<ConditionVO>> searchConByKey(String key)
    {
        Collection<ConditionPO> dataResult = dataService.searchConByKey(key);
        Collection<ConditionVO> bundle = transConsFromData(dataResult);

        return new OperationResult<>(dataResult != null, bundle);
    }

    @Override
    public OperationResult<Collection<ConditionVO>> searchConByOwner(String storeID)
    {
        Collection<ConditionPO> dataResult = dataService.searchConByOwner(storeID);
        Collection<ConditionVO> bundle = transConsFromData(dataResult);

        return new OperationResult<>(dataResult != null, bundle);
    }

    @Override
    public ConditionVO getCondition(String storeID, String conName)
    {
        ConditionPO dataResult = dataService.getCondition(storeID, conName);

        return new ConditionVO(dataResult);
    }

    @Override
    public Collection<ConditionVO> getRandomConditions(int count)
    {
        Iterator<ConditionPO> dataResult = dataService.getRandomConditions(count);
        Collection<ConditionVO> result = new LinkedList<>();

        if (dataResult != null)
        {
            while (dataResult.hasNext())
            {
                result.add(new ConditionVO(dataResult.next()));
            }
        }

        return result;
    }

    @Override
    public OperationResult<ActionVO> addAction(String storeID, String actName, String description, String definition)
    {
        ActionPO actionPO = new ActionPO(storeID, actName, description, definition);
        OperationResult queryResult = dataService.addAction(actionPO);

        OperationResult<ActionVO> result = new OperationResult<>(queryResult);

        if (queryResult.success)
        {
            result.setBundle(new ActionVO(actionPO));
        }

        return result;
    }

    @Override
    public OperationResult deleteAction(String storeID, String actName)
    {
        return dataService.deleteAction(storeID, actName);
    }

    @Override
    public OperationResult<ActionVO> updateAction(String storeID, String actName, String description, String definition)
    {
        ActionPO actionPO = new ActionPO(storeID, actName, description, definition);
        OperationResult dataResult = dataService.updateAction(actionPO);

        OperationResult<ActionVO> result = new OperationResult<>(dataResult);

        if (dataResult.success)
        {
            result.setBundle(new ActionVO(actionPO));
        }

        return result;
    }

    private Collection<ActionVO> transActsFromData(Collection<ActionPO> dataResult)
    {
        if (dataResult == null)
        {
            return null;
        }
        else
        {
            return dataResult.stream().map(ActionVO::new).collect(Collectors.toList());
        }
    }

    @Override
    public OperationResult<Collection<ActionVO>> searchActByKey(String key)
    {
        Collection<ActionPO> dataResult = dataService.searchActByKey(key);
        Collection<ActionVO> bundle = transActsFromData(dataResult);

        return new OperationResult<Collection<ActionVO>>(dataResult != null, bundle);
    }

    @Override
    public OperationResult<Collection<ActionVO>> searchActByOwner(String storeID)
    {
        Collection<ActionPO> dataResult = dataService.searchActByOwner(storeID);
        Collection<ActionVO> bundle = transActsFromData(dataResult);

        return new OperationResult<Collection<ActionVO>>(dataResult != null, bundle);
    }

    @Override
    public ActionVO getAction(String storeID, String actName)
    {
        return new ActionVO(dataService.getAction(storeID, actName));
    }

    @Override
    public Collection<ActionVO> getRandomActions(int count)
    {
        Iterator<ActionPO> dataResult = dataService.getRandomActions(count);
        Collection<ActionVO> bundle = new LinkedList<>();

        if (dataResult == null)
        {
            bundle = null;
        }
        else
        {
            while (dataResult.hasNext())
            {
                bundle.add(new ActionVO(dataResult.next()));
            }
        }

        return bundle;
    }

    public static void main(String[] args)
    {
        ConActImpl impl = new ConActImpl();
        Collection<ConditionVO> vos = impl.getRandomConditions(1);
        for (ConditionVO vo : vos)
        {
            System.out.println(vo.definition);
        }
    }
}
