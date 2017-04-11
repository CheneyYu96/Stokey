package org.xeon.stockey.dataService;

import org.xeon.stockey.po.DailyDataPO;
import org.xeon.stockey.po.StrategyPO;
import org.xeon.stockey.po.UserPO;
import org.xeon.stockey.util.OperationResult;

/**
 * 用户模块的数据层接口
 * Created by Sissel on 2016/6/2.
 */
public interface UserDataService
{
    /**
     * 根据email号获取用户PO
     * @param email email号
     * @return 对应的用户的PO
     */
    public UserPO getUserByEmail(String email);

    /**
     * 根据用户的userID号（用户名）获取用户PO
     * @param userID 用户的用户名号
     * @return 对应的用户的PO
     */
    public UserPO getUserByUserID(String userID);

    /**
     * 根据用户的storeID号获取用户PO
     * @param storeID 用户的内部存储编号
     * @return 对应的用户的PO
     */
    public UserPO getUserByStoreID(String storeID);

    /**
     * 注册新账户
     * @param email 邮箱号
     * @param userID 用户ID号
     * @param password 密码
     * @return 注册是否成功，失败原因（若失败）
     */
    public OperationResult<UserPO> registerAccount(String email, String userID, String password);

    /**
     * 验证邮箱
     * @param email 邮箱号
     * @return 验证是否成功，失败原因（若失败）
     */
    public OperationResult<UserPO> verifyEmail(String email);

    /**
     * 修改用户的密码
     * @param storeID 用户的内部存储编码
     * @param newPassword 新密码
     * @return 密码修改是否成功，失败原因（若失败）
     */
    public OperationResult modifyPassword(String storeID, String newPassword);

    /**
     * 新增一个策略
     * @param newStrategy 新的策略的PO
     * @return 增添是否成功，失败原因（若失败），bundle是作相应修改过的UserPO
     */
    public OperationResult<UserPO> addStrategy(StrategyPO newStrategy);

    /**
     * 删除一个策略
     * @param strategyID 要删除的策略的ID号
     * @return 删除是否成功，失败原因（若失败），bundle是作相应修改过的UserPO
     */
    public OperationResult<UserPO> deleteStrategy(String strategyID);

    /**
     * 修改一个策略
     * @param modifiedStrategy 相应策略修改后的PO，对应的strategyID
     * @return 修改是否成功，失败原因（若失败）
     */
    public OperationResult modifyStrategy(StrategyPO modifiedStrategy);

    /**
     * 新增一个自选股
     * @param storeID 用户的内部存储编码
     * @param stockCode 股票代码
     * @return 新增是否成功，失败原因（若失败），bundle是作相应修改后的UserPO
     */
    public OperationResult<UserPO> addSelection(String storeID, String stockCode);

    /**
     *
     * @param stockCode 股票代码
     * @param storeID 用户的内部存储编码
     * @return 删除是否成功，失败原因（若失败），bundle是作相应修改后的UserPO
     */
    public OperationResult<UserPO> deleteSelection(String storeID, String stockCode);

}
