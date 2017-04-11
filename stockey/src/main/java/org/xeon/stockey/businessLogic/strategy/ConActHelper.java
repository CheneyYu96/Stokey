package org.xeon.stockey.businessLogic.strategy;

import net.sf.json.JSONObject;
import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.businessLogicService.strategyService.ConActService;
import org.xeon.stockey.businessLogicService.strategyService.RecipeService;
import org.xeon.stockey.util.CollectionHelper;
import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.vo.ActionVO;
import org.xeon.stockey.vo.ConditionVO;

import java.io.IOException;
import java.util.Collection;

/**
 *
 * Created by Sissel on 2016/6/19.
 */
public class ConActHelper
{
    ConActService conActService = new ConActImpl();
    RecipeService recipeService = new RecipeImpl();

    enum Type
    {
        ACTION,
        CONDITION
    }

    private OperationResult updateFromFile(String path, Type type, String storeID) throws IOException
    {
        String str = UtilityTools.file2String(path);

        String name = UtilityTools.superTrim(recipeService.extractPart(str, "name"));

        String description = recipeService.extractPart(str, "description");

        switch (type)
        {
            case ACTION:
                return conActService.updateAction(storeID, name, description, str);
            case CONDITION:
                return conActService.updateCondition(storeID, name, description, str);
        }

        return null;
    }

    private OperationResult addFromFile(String path, Type type, String storeID) throws IOException
    {
        String str = UtilityTools.file2String(path);

        String name = UtilityTools.superTrim(recipeService.extractPart(str, "name"));

        String description = recipeService.extractPart(str, "description");

        switch (type)
        {
            case ACTION:
                return conActService.addAction(storeID, name, description, str);
            case CONDITION:
                return conActService.addCondition(storeID, name, description, str);
        }

        return null;
    }

    public static void main2(String[] args) throws IOException
    {
        ConActHelper helper = new ConActHelper();

        OperationResult result = helper.addFromFile("G:\\Repository\\StockEy\\Documentations\\迭代三\\actionExample2.txt", Type.ACTION, "000005");

        System.out.println(result.success);
    }

    // update
    public static void main(String[] args) throws IOException
    {
        ConActHelper helper = new ConActHelper();

        helper.updateFromFile("G:\\Repository\\StockEy\\Documentations\\迭代三\\conditionExample1.txt", Type.CONDITION, "000005");
        helper.updateFromFile("G:\\Repository\\StockEy\\Documentations\\迭代三\\conditionExample2.txt", Type.CONDITION, "000005");
        helper.updateFromFile("G:\\Repository\\StockEy\\Documentations\\迭代三\\conditionExample3.txt", Type.CONDITION, "000005");
        helper.updateFromFile("G:\\Repository\\StockEy\\Documentations\\迭代三\\actionExample1", Type.ACTION, "000005");
        helper.updateFromFile("G:\\Repository\\StockEy\\Documentations\\迭代三\\actionExample2.txt", Type.ACTION, "000005");
    }

    public static void main5(String[] args)
    {
        ConActHelper helper = new ConActHelper();

        Collection<ConditionVO> conditions = helper.conActService.getRandomConditions(4);

        for (ConditionVO conditionVO : conditions)
        {
            System.out.println("name: " + conditionVO.name);
            System.out.println("storeID: " + conditionVO.storeID);
            System.out.println("description: " + conditionVO.description);
            System.out.println("definition: ");
            System.out.println(conditionVO.definition);
            System.out.println("=========================================================");
        }
    }

    public static void main4(String[] args) throws IOException
    {
        ConActHelper helper = new ConActHelper();

        OperationResult result = helper.addFromFile("G:\\Repository\\StockEy\\Documentations\\迭代三\\conditionExample1.txt", Type.CONDITION, "000005");
        System.out.println(result.success);
        result = helper.addFromFile("G:\\Repository\\StockEy\\Documentations\\迭代三\\conditionExample2.txt", Type.CONDITION, "000005");
        System.out.println(result.success);
        result = helper.addFromFile("G:\\Repository\\StockEy\\Documentations\\迭代三\\conditionExample3.txt", Type.CONDITION, "000005");
        System.out.println(result.success);
    }

    public static void main1(String[] args)
    {
        ConActHelper helper = new ConActHelper();

        Collection<ActionVO> actionVOs = helper.conActService.getRandomActions(4);

        for (ActionVO actionVO : actionVOs)
        {
            System.out.println("name: " + actionVO.name);
            System.out.println("storeID: " + actionVO.storeID);
            System.out.println("description: " + actionVO.description);
            System.out.println("definition: ");
            System.out.println(actionVO.definition);
        }
    }
}
