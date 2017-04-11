package org.xeon.stockey.dataService;

import org.xeon.stockey.po.ActionPO;
import org.xeon.stockey.po.ConditionPO;
import org.xeon.stockey.util.OperationResult;

import java.util.Collection;
import java.util.Iterator;

/**
 * 条件和动作模块的数据层接口
 * Created by Sissel on 2016/6/16.
 */
public interface ConActDataService
{
    /**
     * 增加条件定义
     * @param conditionPO 条件定义
     * @return 操作结果
     */
    public OperationResult addCondition(ConditionPO conditionPO);

    /**
     * 删除条件定义
     * @param storeID 用户的store编号
     * @param conName 条件名称
     * @return 操作结果
     */
    public OperationResult deleteCondition(String storeID, String conName);

    /**
     * 更新条件定义
     * @param conditionPO 修改后的条件定义
     * @return 操作结果，bundle是操作后的动作定义
     */
    public OperationResult<ConditionPO> updateCondition(ConditionPO conditionPO);

    /**
     * 用关键字搜索条件，从条件的名字，描述那里筛选符合的条件
     * @param key 关键字
     * @return 操作结果，bundle是符合的Condition
     */
    public Collection<ConditionPO> searchConByKey(String key);

    /**
     * 用 用户storeID搜索条件
     * @param storeID 用户的storeID
     * @return 操作结果，bundle是符合的Condition
     */
    public Collection<ConditionPO> searchConByOwner(String storeID);

    /**
     * 获取指定的一个条件
     * @param storeID 制作者的storeID
     * @param conName 条件名称
     * @return 指定的Condition的PO
     */
    public ConditionPO getCondition(String storeID, String conName);

    /**
     * 随机获取几个Condition
     * @param count 要获取的数目
     * @return count个数目的ConditionPO，可能少于count
     */
    public Iterator<ConditionPO> getRandomConditions(int count);

    /**
     * 新增动作定义
     * @param actionPO 动作定义
     * @return 操作结果
     */
    public OperationResult addAction(ActionPO actionPO);

    /**
     * 删除动作定义
     * @param storeID 用户的storeID
     * @param actName 动作名称
     * @return 操作结果
     */
    public OperationResult deleteAction(String storeID, String actName);

    /**
     * 更新动作定义
     * @param actionPO 更新后的动作定义
     * @return 操作结果，bundle是操作后的动作定义
     */
    public OperationResult<ActionPO> updateAction(ActionPO actionPO);

    /**
     * 用关键字搜索动作，从动作的名字，描述那里筛选符合的动作
     * @param key 关键字
     * @return 操作结果，bundle是符合的Action
     */
    public Collection<ActionPO> searchActByKey(String key);

    /**
     * 用关键字搜索，从动作的名字，描述那里筛选符合的动作
     * @param storeID 用户的storeID
     * @return 操作结果，bundle是符合的Action
     */
    public Collection<ActionPO> searchActByOwner(String storeID);

    /**
     * 获取指定的Action
     * @param storeID 制作者的storeID
     * @param actName 动作的名称
     * @return 指定的Action的PO
     */
    public ActionPO getAction(String storeID, String actName);

    /**
     * 获取任意个随机的动作
     * @param count 要获取的动作的数量
     * @return count个ActionPO，可能少于count
     */
    public Iterator<ActionPO> getRandomActions(int count);
}
