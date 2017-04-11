package org.xeon.stockey.businessLogic.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xeon.stockey.businessLogic.stockAccess.DBStockSourceImpl;
import org.xeon.stockey.businessLogic.stockAccess.FileStockSourceImpl;
import org.xeon.stockey.businessLogic.utility.NetworkConnectionException;
import org.xeon.stockey.businessLogic.utility.StringChecker;
import org.xeon.stockey.businessLogicService.stockAccessService.StockSourceService;
import org.xeon.stockey.businessLogicService.userService.UserService;
import org.xeon.stockey.data.impl.DataServiceFactory;
import org.xeon.stockey.data.impl.MySessionFactory;
import org.xeon.stockey.data.impl.UserData;
import org.xeon.stockey.dataService.UserDataService;
import org.xeon.stockey.po.StrategyPO;
import org.xeon.stockey.po.UserPO;
import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.vo.StockInfoVO;
import org.xeon.stockey.vo.StrategyVO;
import org.xeon.stockey.vo.UserVO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * UserService 接口的实现部分
 * Created by Sissel on 2016/5/31.
 */
@SpringBootApplication
@RestController
@RequestMapping("/user")
public class UserImpl implements UserService
{
    private static Set<Character> validPwdSet;
    private static Set<Character> validIDSet;

    private static <T> OperationResult<T> successResult(T po)
    {
        return new OperationResult<T>(true,po);
    }

    private static <T> OperationResult<T> successResult()
    {
        return new OperationResult<>(true);
    }

    private static <T> OperationResult<T> failResult(String failReason)
    {
        return new OperationResult<>(false, failReason);
    }

    private static OperationResult<UserVO> checkPassword(String password)
    {
        // if password matches the lowest standard
        if (password.length() < 8)
        {
            return failResult("密码过短");
        }
        int score = 0;
        score += StringChecker.hasLowercase(password) ? 1 : 0;
        score += StringChecker.hasUppercase(password) ? 1 : 0;
        score += StringChecker.hasNumber(password) ? 1 : 0;
        if (score < 2)
        {
            return failResult("密码安全性过低，至少包含大写、小写、数字之中的两个");
        }

        return successResult();
    }

    private UserPO getUserPO(String emailOrID)
    {
        return StringChecker.isEmailFormat(emailOrID) ?
                userDataService.getUserByEmail(emailOrID) :
                userDataService.getUserByUserID(emailOrID);
    }

    private UserDataService userDataService;
    private StockSourceService stockSourceService;

    static
    {
        // init charset
        validPwdSet = new HashSet<>();
        validIDSet = new HashSet<>();
        Character[] chars = new Character[]{'!', '@', '#', '$', '%', '^', '&', '*', '_'};
        Collections.addAll(validPwdSet, chars);

        // add characters
        for (int i = 0; i < 26; i++)
        {
            // add uppercase
            validPwdSet.add((char) (65 + i));
            validIDSet.add((char) (65 + i));
            // add lowercase
            validPwdSet.add((char) (97 + i));
            validIDSet.add((char) (97 + i));
        }

        // add numbers
        for (int i = 0; i < 10; i++)
        {
            validPwdSet.add((char) (48 + i));
            validIDSet.add((char) (48 + i));
        }
    }

    public UserImpl()
    {
        userDataService = DataServiceFactory.getUserDataService();
        stockSourceService = new DBStockSourceImpl();
    }

    @Override
    @RequestMapping("/registerAccount")
    public OperationResult registerAccount(
            @RequestParam("email") String email,
            @RequestParam("userID") String userID,
            @RequestParam("password") String password)
    {
        // if invalid characters contained
        if (!StringChecker.allCharsValid(userID, validIDSet))
        {
            return failResult("用户ID包含无效字符");
        }
        if (!StringChecker.allCharsValid(password, validPwdSet))
        {
            return failResult("密码包含无效字符");
        }
        if (!StringChecker.isEmailFormat(email))
        {
            return failResult("邮件格式不正确");
        }

        // check password
        OperationResult pwdCheckResult = checkPassword(password);
        if (!pwdCheckResult.success)
        {
            return pwdCheckResult;
        }

        // whether email and id has been registered
        OperationResult dataResult = userDataService.registerAccount(email, userID, password);
        if (dataResult.success)
        {
            //link example: localhost:9090/user/verifyEmail?email=niansong1996@163.com
            String verificationLink = String.format("localhost:9090/user/verifyEmail?email=%s", email);
            MailModule.sendVerificationEmail(email, verificationLink);
            return successResult();
        }
        else
        {
            return dataResult;
        }
    }

    @Override
    @RequestMapping("/logIn")
    public OperationResult<UserPO> logIn(
            @RequestParam("email") String emailOrID,
            @RequestParam("password") String password)
    {
        UserPO searchResult = getUserPO(emailOrID);

        if (searchResult == null)
        {
            return failResult("该账户不存在");
        }
        else
        {
            // password matches
            if (searchResult.getPassword().equals(password))
            {
                return successResult(searchResult);
            }
            else
            {
                return failResult("密码不匹配");
            }
        }
    }

    @Override
    @RequestMapping("/verifyEmail")
    public OperationResult verifyEmail(
            @RequestParam("email") String email)
    {
        if (StringChecker.isEmailFormat(email))
        {
            UserPO userPO = userDataService.getUserByEmail(email);
            if (userPO != null)
            {
                return userDataService.verifyEmail(email);
            }
            else
            {
                return failResult("该用户不存在");
            }
        }
        else
        {
            return failResult("邮箱格式不正确");
        }
    }

    @Override
    @RequestMapping("/logOut")
    public OperationResult logOut(
            @RequestParam("storeID") String storeID)
    {
        return successResult();
    }

    @Override
    @RequestMapping("/modifyPassword")
    public OperationResult<UserVO> modifyPassword(
            @RequestParam("storeID") String storeID,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword)
    {
        UserPO userPO = userDataService.getUserByStoreID(storeID);

        if (userPO == null)
        {
            return failResult("该账户不存在");
        }
        else
        {
            // check old password
            if (!oldPassword.equals(userPO.getPassword()))
            {
                return failResult("密码不匹配");
            }

            // check new password
            OperationResult<UserVO> pwdCheckResult = checkPassword(newPassword);
            if (!pwdCheckResult.success)
            {
                return pwdCheckResult;
            }

            // all pass, modify in data layer
            userDataService.modifyPassword(storeID, newPassword);
            return successResult();
        }
    }

    @Override
    @RequestMapping("/getUserInfo")
    public UserVO getUserInfo(
            @RequestParam("emailOrID") String emailOrID)
    {
        UserPO userPO = getUserPO(emailOrID);

        if (userPO == null)
        {
            return null;
        }
        else
        {
            return new UserVO(userPO);
        }
    }

    @Override
    @RequestMapping("/getStrategiesByUser")
    public Collection<StrategyVO> getStrategiesByUser(
            @RequestParam("storeID") String storeID)
    {
        // find out is id or email
        UserPO userPO = userDataService.getUserByStoreID(storeID);

        if (userPO == null)
        {
            return null;
        }
        else
        {
            // iterate the strategies to make the result
            return userPO.getStrategies()
                    .stream()
                    .map(StrategyVO::new)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @RequestMapping("/getSelectionsByUser")
    public Collection<StockInfoVO> getSelectionsByUser(
            @RequestParam("storeID") String storeID) throws NetworkConnectionException
    {
        // find out is id or email
        UserPO userPO = userDataService.getUserByStoreID(storeID);

        if (userPO == null)
        {
            return null;
        }
        else
        {
            LinkedList<StockInfoVO> stockInfoVOs = new LinkedList<>();
            for (String selection : userPO.getSelections())
            {
                stockInfoVOs.add(stockSourceService.getStockInfo(selection));
            }
            return stockInfoVOs;
        }
    }

    @Override
    public OperationResult<UserVO> addStrategy(
            StrategyVO newStrategy)
    {
        // find out is id or email
        UserPO userPO = getUserPO(newStrategy.ownerID);

        if (userPO == null)
        {
            return failResult("该账户不存在");
        }
        else
        {
            StrategyPO strategyPO = newStrategy.toStrategyPO();
            @SuppressWarnings("modified by nians on 2016/6/2 to fit interface" +
                    "origin is :" +
                    "OperationResult<UserPO> dataResult = userDataService.addStrategy(userPO.getUserStoreID(), strategyPO);")
            OperationResult<UserPO> dataResult = userDataService.addStrategy(strategyPO);
            OperationResult<UserVO> result = new OperationResult<>(dataResult);
            result.setBundle(new UserVO(dataResult.getBundle()));

            return result;
        }
    }

    @Override
    @RequestMapping("/strategy/add")
    public OperationResult<UserVO> addStrategy(
            @RequestParam("storeID") String storeID,
            @RequestParam("strategyName") String strategyName,
            @RequestParam("code") String code)
    {
        // find out is id or email
        UserPO userPO = userDataService.getUserByStoreID(storeID);

        if (userPO == null)
        {
            return failResult("该账户不存在");
        }
        else
        {
            StrategyPO strategyPO = new StrategyPO();
            strategyPO.setUserStoreID(storeID);
            strategyPO.setContent(code);
            strategyPO.setName(strategyName);
            @SuppressWarnings("modified by nians on 2016/6/2 to fit interface" +
                    "origin is :" +
                    "OperationResult<UserPO> dataResult = userDataService.addStrategy(userPO.getUserStoreID(), strategyPO);")
            OperationResult<UserPO> dataResult = userDataService.addStrategy(strategyPO);
            OperationResult<UserVO> result = new OperationResult<>(dataResult);
            result.setBundle(new UserVO(dataResult.getBundle()));

            return result;
        }
    }

    @Override
    @RequestMapping("/strategy/delete")
    public OperationResult<UserVO> deleteStrategy(
            @RequestParam("strategyID") String strategyID)
    {
        @SuppressWarnings("modified by nians on 2016/6/2 to fit interface" +
                "origin is :" +
                "            OperationResult<UserPO> dataResult = userDataService.deleteStrategy(userPO.getUserStoreID(), strategyID);")
        OperationResult<UserPO> dataResult = userDataService.deleteStrategy(strategyID);
        OperationResult<UserVO> result = new OperationResult<>(dataResult);
        result.setBundle(new UserVO(dataResult.getBundle()));

        return result;
    }

    @Override
    public OperationResult modifyStrategy(
            StrategyVO modifiedStrategy)
    {
        StrategyPO strategyPO = modifiedStrategy.toStrategyPO();
        return userDataService.modifyStrategy(strategyPO);
    }

    @Override
    @RequestMapping("/strategy/modify")
    public OperationResult modifyStrategy(
            @RequestParam("strategyID") String strategyID,
            @RequestParam("newCode") String newCode,
            @RequestParam("newName") String newName)
    {
        StrategyPO strategyPO = new StrategyPO();
        strategyPO.setName(newName);
        strategyPO.setContent(newCode);
        return userDataService.modifyStrategy(strategyPO);
    }

    @Override
    @RequestMapping("/selection/add")
    public OperationResult<UserVO> addSelection(
            @RequestParam("emailOrID") String emailOrID,
            @RequestParam("stockCode") String stockCode)
    {
        UserPO userPO = getUserPO(emailOrID);

        if (userPO == null)
        {
            return failResult("该账户不存在");
        }
        else
        {
            OperationResult<UserPO> dataResult = userDataService.addSelection(userPO.getStoreID(), stockCode);
            OperationResult<UserVO> result = new OperationResult<>(dataResult);
            result.setBundle(new UserVO(dataResult.getBundle()));

            return result;
        }
    }

    @Override
    @RequestMapping("/selection/delete")
    public OperationResult<UserVO> deleteSelection(
            @RequestParam("emailOrID") String emailOrID,
            @RequestParam("stockCode") String stockCode)
    {
        UserPO userPO = getUserPO(emailOrID);

        if (userPO == null)
        {
            return failResult("该账户不存在");
        }
        else
        {
            OperationResult<UserPO> dataResult = userDataService.deleteSelection(userPO.getStoreID(), stockCode);
            OperationResult<UserVO> result = new OperationResult<>(dataResult);
            result.setBundle(new UserVO(dataResult.getBundle()));

            return result;
        }
    }
}
