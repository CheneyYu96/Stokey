package org.xeon.stockey.businessLogicService.userService;

import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.vo.StockInfoVO;
import org.xeon.stockey.vo.StrategyVO;
import org.xeon.stockey.vo.UserVO;

import java.util.Collection;

/**
 * some functions related to user
 * Created by Sissel on 2016/5/30.
 */
public interface UserService
{
    /**
     * 用户注册
     * @param email 邮箱号
     * @param userID 用户ID号
     * @param password 用户密码，重复密码检验在UI处处理, 密码只能包含 大小写字母，数字，_!@#$%这几个字符
     * @return 有无注册成功，失败原因（若失败)
     */
    public OperationResult registerAccount(String email, String userID, String password);

    /**
     * 用户登录，若有验证码，在UI处处理
     * @param emailOrID 邮箱号或ID号
     * @param password 用户密码
     * @return 有无登录成功，失败原因（若失败)
     */
    public OperationResult logIn(String emailOrID, String password);

    /**
     * 验证邮箱
     * @param email 邮箱号
     * @return 邮箱是否检验成功，失败原因（若失败)
     */
    public OperationResult verifyEmail(String email);

    /**
     * 用户注销
     * @param storeID 内部存储编号
     * @return 是否注销成功，失败原因（若失败）
     */
    public OperationResult logOut(String storeID);

    /**
     * 用户修改密码
     * @param storeID 内部存储编号
     * @param oldPassword 旧密码
     * @param newPassword 新密码，密码只能包含 大小写字母，数字，_!@#$%这几个字符
     * @return 是否修改成功，失败原因（若失败）
     */
    public OperationResult modifyPassword(String storeID, String oldPassword, String newPassword);

    /**
     * 获取用户信息
     * @param emailOrID 邮箱或ID
     * @return 包含用户信息的VO对象
     */
    public UserVO getUserInfo(String emailOrID);

    /**
     * 获取特定用户的策略
     * @param storeID 内部存储编号
     * @return 该用户的所有策略的VO
     */
    public Collection<StrategyVO> getStrategiesByUser(String storeID);

    /**
     * 获取特定用户的自选股
     * @param storeID 内部存储编号
     * @return 该用户的所有自选股的VO
     */
    public Collection<StockInfoVO> getSelectionsByUser(String storeID) throws NetworkConnectionException;

    /**
     * 新增一个策略
     * @param newStrategy 新的策略的VO
     * @return 增添是否成功，失败原因（若失败），bundle是作相应修改过的UserVO
     */
    public OperationResult<UserVO> addStrategy(StrategyVO newStrategy);

    /**
     * 新增一个策略
     * @param storeID 用户（创作者）的内部存储编号
     * @param strategyName 策略的名字
     * @param code 策略的代码
     * @return 增添是否成功，失败原因（若失败），bundle是作相应修改过的UserVO
     */
    public OperationResult<UserVO> addStrategy(String storeID, String strategyName, String code);

    /**
     * 删除一个策略
     * @param strategyID 要删除的策略的ID号
     * @return 删除是否成功，失败原因（若失败），bundle是作相应修改过的UserVO
     */
    public OperationResult<UserVO> deleteStrategy(String strategyID);

    /**
     * 修改一个策略
     * @param modifiedStrategy 相应策略修改后的VO，对应的strategyID
     * @return 修改是否成功，失败原因（若失败）
     */
    public OperationResult modifyStrategy(StrategyVO modifiedStrategy);

    /**
     * 修改一个策略
     * @param strategyID 策略的ID号
     * @param newCode 策略的修改后的代码，若无修改，传原本的代码
     * @param newName 策略的修改后的名称，若无修改，传原本的名字
     * @return 修改是否成功，失败原因（若失败）
     */
    public OperationResult modifyStrategy(String strategyID, String newCode, String newName);

    /**
     * 新增一个自选股
     * @param storeID 用户的内部存储编号
     * @param stockCode 股票代码
     * @return 新增是否成功，失败原因（若失败），bundle是作相应修改后的UserVO
     */
    public OperationResult<UserVO> addSelection(String storeID, String stockCode);

    /**
     *
     * @param storeID 用户的内部存储编号
     * @param stockCode 股票代码
     * @return 删除是否成功，失败原因（若失败），bundle是作相应修改后的UserVO
     */
    public OperationResult<UserVO> deleteSelection(String storeID, String stockCode);
}
