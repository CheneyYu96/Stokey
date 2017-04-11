package org.xeon.stockey.businessLogicService.userService;

import org.junit.Test;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.xeon.stockey.businessLogic.user.UserImpl;
import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.vo.StockInfoVO;
import org.xeon.stockey.vo.UserVO;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * 用户模块的测试
 * Created by Sissel on 2016/6/5.
 */
public class UserServiceTest
{
    UserService userService;
    UserVO userVO;

    public UserServiceTest()
    {
        userService = new UserImpl();
        userVO = userService.getUserInfo("chengbutang@163.com");
    }

    @Test
    public void testRegisterAccount() throws Exception
    {
        OperationResult result = userService.registerAccount("chengbutang@163.com", "wjr14", "1234abcde");
        System.out.println(result.success);
        System.out.println(result.reason);
    }

    @Test
    public void testLogIn() throws Exception
    {

    }

    @Test
    public void testVerifyEmail() throws Exception
    {

    }

    @Test
    public void testLogOut() throws Exception
    {

    }

    @Test
    public void testModifyPassword() throws Exception
    {

    }

    @Test
    public void testGetUserInfo() throws Exception
    {

    }

    @Test
    public void testGetStrategiesByUser() throws Exception
    {

    }

    @Test
    public void testGetSelectionsByUser() throws Exception
    {
        String storeID = userVO.storeID;

        Collection<StockInfoVO> collection = userService.getSelectionsByUser(storeID);
        System.out.println(collection.size());
        for (StockInfoVO stockInfoVO : collection)
        {
            System.out.println(stockInfoVO.toString());
        }
    }

    @Test
    public void testAddStrategy() throws Exception
    {

    }

    @Test
    public void testAddStrategy1() throws Exception
    {

    }

    @Test
    public void testDeleteStrategy() throws Exception
    {

    }

    @Test
    public void testModifyStrategy() throws Exception
    {

    }

    @Test
    public void testModifyStrategy1() throws Exception
    {

    }

    @Test
    public void testAddSelection() throws Exception
    {
        OperationResult result = userService.addSelection(userVO.userID, "sh600606");
        System.out.println(result.success + " " + result.reason);
    }

    @Test
    public void testDeleteSelection() throws Exception
    {

    }
}