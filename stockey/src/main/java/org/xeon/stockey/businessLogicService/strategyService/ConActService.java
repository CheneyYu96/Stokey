package org.xeon.stockey.businessLogicService.strategyService;

import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.vo.ActionVO;
import org.xeon.stockey.vo.ConditionVO;

import java.util.Collection;

/**
 * 条件和动作的逻辑层接口
 * Created by Sissel on 2016/6/16.
 */
public interface ConActService
{
    /************************************  Condition  **********************************/

    /**
     * 增加新条件
     * @param storeID 用户的storeID
     * @param conName 条件的名字
     * @param description 条件的描述
     * @param definition 条件的定义
     * @return 操作结果，bundle是组装后的ConditionVO
     */
    public OperationResult<ConditionVO> addCondition(String storeID,
                                        String conName,
                                        String description,
                                        String definition);

    /**
     * 删除条件
     * @param storeID 用户的storeID
     * @param conName 条件的名字
     * @return 操作结果
     */
    public OperationResult deleteCondition(String storeID, String conName);

    /**
     * 更新条件定义
     * @param storeID 用户的storeID
     * @param conName 条件的名字
     * @param description 条件的描述
     * @param definition 条件的定义
     * @return 操作结果，bundle是组装后的ConditionVO
     */
    public OperationResult<ConditionVO> updateCondition(String storeID,
                                           String conName,
                                           String description,
                                           String definition);

    /**
     * 用关键字搜索条件，从条件的名字和描述中筛选符合的条件
     * @param key 关键字
     * @return 操作结果，bundle是符合的ConditionVO的集合
     */
    public OperationResult<Collection<ConditionVO>> searchConByKey(String key);

    /**
     * 用 定义者的storeID搜索条件
     * @param storeID 用户的storeID
     * @return 操作结果，bundle是符合的ConditionVO的集合
     */
    public OperationResult<Collection<ConditionVO>> searchConByOwner(String storeID);

    /**
     * 获取指定的一个条件
     * @param storeID 制作者的storeID
     * @param conName 条件名称
     * @return 指定的Condition的VO
     */
    public ConditionVO getCondition(String storeID, String conName);

    /**
     * 随机获取几个Condition
     * @param count 要获取的数目
     * @return count个数目的ConditionVO，可能少于count
     */
    public Collection<ConditionVO> getRandomConditions(int count);


    /******************************** Action **********************************/


    /**
     * 增加动作
     * @param storeID 用户的storeID
     * @param actName 动作的名字
     * @param description 动作的描述
     * @param definition 动作的定义
     * @return 操作结果，bundle是组装后的ActionVO
     */
    public OperationResult<ActionVO> addAction(String storeID,
                                     String actName,
                                     String description,
                                     String definition);

    /**
     * 删除动作
     * @param storeID 用户的storeID
     * @param actName 动作的名字
     * @return 操作结果
     */
    public OperationResult deleteAction(String storeID, String actName);

    /**
     * 更新动作
     * @param storeID 用户的storeID
     * @param actName 动作的名字
     * @param description 动作的描述
     * @param definition 动作的定义
     * @return 操作结果，bundle是组装后的ActionVO
     */
    public OperationResult<ActionVO> updateAction(String storeID,
                                        String actName,
                                        String description,
                                        String definition);

    /**
     * 用关键字搜索动作，从动作的名字和描述里找到符合的
     * @param key 关键字
     * @return 操作结果，bundle是符合的ActionVO的集合
     */
    public OperationResult<Collection<ActionVO>> searchActByKey(String key);

    /**
     * 用定义者storeID搜索动作
     * @param storeID 定义者的storeID
     * @return 操作结果，bundle是符合的ActionVO的集合
     */
    public OperationResult<Collection<ActionVO>> searchActByOwner(String storeID);

    /**
     * 获取指定的Action
     * @param storeID 制作者的storeID
     * @param actName 动作的名称
     * @return 指定的Action的VO
     */
    public ActionVO getAction(String storeID, String actName);

    /**
     * 获取任意个随机的动作
     * @param count 要获取的动作的数量
     * @return count个ActionVO，可能少于count
     */
    public Collection<ActionVO> getRandomActions(int count);
}
