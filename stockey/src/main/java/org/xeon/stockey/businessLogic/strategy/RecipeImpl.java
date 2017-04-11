package org.xeon.stockey.businessLogic.strategy;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.IterableGet;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xeon.stockey.businessLogic.strategy.intermediateRepresentation.ActionIR;
import org.xeon.stockey.businessLogic.strategy.intermediateRepresentation.ConditionIR;
import org.xeon.stockey.businessLogic.utility.UtilityTools;
import org.xeon.stockey.businessLogicService.strategyService.ConActService;
import org.xeon.stockey.businessLogicService.strategyService.RecipeService;
import org.xeon.stockey.util.OperationResult;
import org.xeon.stockey.vo.ActionVO;
import org.xeon.stockey.vo.ConditionVO;

import java.io.StringReader;
import java.io.UTFDataFormatException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Recipe接口的实现
 * Created by Sissel on 2016/6/17.
 */
@RestController
@RequestMapping("/strategy/recipe")
public class RecipeImpl implements RecipeService
{
    // key:   identifier = storeID(6) + conName
    // value: the definition of the Condition
    private Map<String, String> conDefMap;

    // key:   identifier = storeID(6) + actName
    // value: the definition of the Action
    private Map<String, String> actDefMap;

    //
    private List<String> importPool;

    // key:    moduleName
    // value:  components
    private Map<String, Set<String>> importMap;

    private ConActService conActService;

    private int conditionCount = 0;
    private int actionCount = 0;

    public RecipeImpl()
    {
        conDefMap = new HashedMap<>();
        actDefMap = new HashMap<>();
        importPool = new LinkedList<>();
        importMap = new HashMap<>();
        importMap.put("$MODULES", new HashSet<>());

        conActService = new ConActImpl();
    }

    private String getConDef(String identifier) throws Exception
    {
        if (conDefMap.get(identifier) == null)
        {
            String storeID = identifier.substring(0, 6);
            String name = identifier.substring(6);

            OperationResult<Collection<ConditionVO>> queryResult = conActService.searchConByOwner(storeID);
            if (queryResult.success)
            {
                Collection<ConditionVO> conditions = queryResult.getBundle();
                for (ConditionVO condition : conditions)
                {
                    if (condition.getName().equals(name))
                    {
                        conDefMap.put(identifier, condition.getDefinition());
                        return condition.getDefinition();
                    }
                }

                throw new Exception();
            }
            else
            {
                throw new Exception();
            }
        }

        return conDefMap.get(identifier);
    }

    private String getActDef(String identifier) throws Exception
    {
        if (actDefMap.get(identifier) == null)
        {
            String storeID = identifier.substring(0, 6);
            String name = identifier.substring(6);

            OperationResult<Collection<ActionVO>> queryResult = conActService.searchActByOwner(storeID);
            if (queryResult.success)
            {
                Collection<ActionVO> actions = queryResult.getBundle();
                for (ActionVO action : actions)
                {
                    if (action.getName().equals(name))
                    {
                        actDefMap.put(identifier, action.getDefinition());
                        return action.getDefinition();
                    }
                }

                throw new Exception();
            }
            else
            {
                throw new Exception();
            }
        }

        return actDefMap.get(identifier);
    }

    @Override
    public String extractPart(String origin, String partName)
    {
        String[] lines = origin.split("\n");
        StringBuilder sb = new StringBuilder();
        boolean start = false;

        for (String line : lines)
        {
            if (line.startsWith("&&" + partName))
            {
                start = true;
            }
            else if (line.startsWith("&&") && start)
            {
                break;
            }
            else if (start)
            {
                sb.append(line);
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private String renameVariables(String raw, String id)
    {
        return raw.replaceAll("\\$", id + "_");
    }

    private String replaceArgs(String template, String param, String arg)
    {
        Pattern pattern = Pattern.compile("(" + "\\$" + param + ")" + "([^0-9a-zA-Z_])");
        Matcher matcher = pattern.matcher(template);
        List<Integer> positions = new LinkedList<>();
        while (matcher.find())
        {
            positions.add(matcher.start());
        }

        Collections.reverse(positions);
        int paramLength = param.length() + 1;
        for (Integer position : positions)
        {
            template = UtilityTools.replaceStr(position, paramLength, template, arg);
        }

        return template;
    }

    private List<String> getParams(String def)
    {
        String paramStr = extractPart(def, "variable");
        String[] params = paramStr.split(",");
        List<String> result = new LinkedList<>();

        for (String param : params)
        {
            result.add(param.replaceFirst("=.*", "").trim());
        }

        return result;
    }

    private void putInImportPool(String importStats)
    {
        String[] stats = importStats.split("\n");
        for (String stat : stats)
        {
            if (stat.contains("import"))
            {
                importPool.add(stat);
            }
        }
    }

    private ConditionIR parseSingleCondition(JSONObject json, String stockCode) throws Exception
    {
        String identifier = json.getString("condition_id");
        JSONArray argsArray = json.getJSONArray("args");
        String def = getConDef(identifier);

        // put in importPool
        putInImportPool(extractPart(def, "const"));

        // replace args with params
        String temp = replaceArgs(def, "stock", stockCode);
        List<String> params = getParams(def);

        assert params.size() == argsArray.size();

        for (int i = 0; i < argsArray.size(); i++)
        {
            String param = params.get(i);
            String arg = argsArray.getString(i);

            temp = replaceArgs(temp, param, arg);
        }

        // rename other vars to avoid conflicts
        temp = renameVariables(temp, "con" + conditionCount);
        ++conditionCount;

        String compute = extractPart(temp, "compute");
        String boolExpr = UtilityTools.superTrim(extractPart(temp, "condition"));

        ConditionIR result = new ConditionIR(0, compute, boolExpr);

        return result;
    }

    private ConditionIR parseCondition(JSONObject json, String stockCode) throws Exception
    {
        if (json.getString("type").equals("single"))
        {
            return parseSingleCondition(json.getJSONObject("condition1"), stockCode);
        }
        else
        {
            ConditionIR conditionIR1, conditionIR2 = null;
            conditionIR1 = parseCondition(json.getJSONObject("condition1"), stockCode);
            if (!json.getString("type").equals("not"))
            {
                conditionIR2 = parseCondition(json.getJSONObject("condition2"), stockCode);
            }

            switch (json.getString("type"))
            {
                case "not":
                    return ConditionIR.notConditionIR(conditionIR1);
                case "and":
                    return ConditionIR.andConditionIR(conditionIR1, conditionIR2);
                case "or":
                    return ConditionIR.orConditionIR(conditionIR1, conditionIR2);
                default:
                    System.err.println("type error in RecipeImpl.parseCondition");
                    return null;
            }
        }
    }

    private ActionIR parseSingleAction(JSONObject json, String stockCode) throws Exception
    {
        String identifier = json.getString("action_id");
        JSONArray argsArray = json.getJSONArray("args");
        String def = getActDef(identifier);

        // put in importPool
        putInImportPool(extractPart(def, "const"));

        // replace args with params
        String temp = replaceArgs(def, "stock", stockCode);
        List<String> params = getParams(def);

        assert params.size() == argsArray.size();

        for (int i = 0; i < argsArray.size(); i++)
        {
            String param = params.get(i);
            String arg = argsArray.getString(i);

            temp = replaceArgs(temp, param, arg);
        }

        // rename other vars to avoid conflicts
        temp = renameVariables(temp, "act" + actionCount);
        ++actionCount;

        String compute = extractPart(temp, "compute");

        ActionIR result = new ActionIR(0, compute);

        return result;
    }

    private ActionIR parseAction(JSONObject json, String stockCode) throws Exception
    {
        if (json.getString("type").equals("single"))
        {
            return parseSingleAction(json.getJSONObject("value"), stockCode);
        }

        assert json.getString("type").equals("single");
        return null;
    }

    private String combineConAct(ConditionIR conditionIR, ActionIR actionIR)
    {
        // TODO: 2016/6/19 strong restriction
        return conditionIR.toFinalRepresentation() + "\n    " + actionIR.toFinalRepresentation();
    }

    private String mergeImports(Collection<String> importStatements)
    {
        for (String importStatement : importStatements)
        {
            String[] segs = importStatement.split("import");
            if (segs[0].contains("from"))
            {
                String moduleName = (segs[0].split(" "))[1];
                String[] componentStr = segs[1].split(",");

                Set<String> componentSet = new HashSet<>();
                for (String com : componentStr)
                {
                    componentSet.add(com.trim());
                }

                Set<String> oldSet = importMap.get(moduleName);
                if (oldSet != null)
                {
                    oldSet.addAll(componentSet);
                }
                else
                {
                    importMap.put(moduleName, componentSet);
                }
            }
            else
            {
                String[] modules = segs[1].split(",");
                Set<String> moduleSet = importMap.get("$MODULES");
                for (String module : modules)
                {
                    moduleSet.add(module.trim());
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        Iterator<String> modules = importMap.get("$MODULES").iterator();
        if (modules.hasNext())
        {
            sb.append("import ");

            while (modules.hasNext())
            {
                sb.append(modules.next());
                if (modules.hasNext())
                {
                    sb.append(",");
                }
            }

            sb.append("\n");
        }

        Set<String> keySet = importMap.keySet();
        for (String key : keySet)
        {
            if (key.equals("$MODULES"))
                continue;

            sb.append("from ");
            sb.append(key);
            sb.append(" import ");
            Iterator<String> components = importMap.get(key).iterator();

            while (components.hasNext())
            {
                sb.append(components.next());
                if (components.hasNext())
                {
                    sb.append(",");
                }
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private String addIndents(String code, int indent_count)
    {
        String[] lines = code.split("\n");
        String spaces = "";
        for (int i = 0; i < indent_count; i++)
        {
            spaces += "    ";
        }

        StringBuilder sb = new StringBuilder();
        for (String line : lines)
        {
            sb.append(spaces);
            sb.append(line);
            sb.append("\n");
        }

        return sb.toString();
    }

    private String makeHandleData(String recipesCode)
    {
        String result = "def handle_data(context, data): \n";

        result += addIndents(recipesCode, 1);

        return result;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "run")
    public OperationResult<String> compile(@RequestBody String rawText)
    {
        JSONObject json = JSONObject.fromObject(rawText);
        OperationResult<String> result;

        try
        {
            JSONArray recipes = json.getJSONArray("recipes");
            String stockCode = json.getString("stockCode");
            StringBuilder sb = new StringBuilder();

            // compile recipes
            for (int i = 0; i < recipes.size(); i++)
            {
                JSONObject recipe = recipes.getJSONObject(i);
                JSONObject condition = recipe.getJSONObject("condition");
                JSONObject action = recipe.getJSONObject("action");

                ConditionIR conditionIR = parseCondition(condition, stockCode);
                ActionIR actionIR = parseAction(action, stockCode);

                sb.append(combineConAct(conditionIR, actionIR));
                sb.append("\n");
            }

            String importHead = mergeImports(importPool);

            String initPart = "def initialize(context): \n    return\n\n";

            String handleDataPart = makeHandleData(sb.toString());

            result = new OperationResult<>(true);
            result.setBundle(importHead + initPart + handleDataPart);

            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new OperationResult<>(false);
            result.reason = "Exception happened";

            return result;
        }
    }

    // whole compile
    public static void main(String[] args)
    {
        JSONObject singleCon = new JSONObject();
        JSONArray conArgs = new JSONArray();
        conArgs.add(12);
        conArgs.add(26);
        singleCon.element("condition_id", "000005slide");
        singleCon.element("args", conArgs);
        JSONObject condition = new JSONObject();
        condition.element("type", "single");
        condition.element("condition1", singleCon);

        JSONObject singleCon2 = new JSONObject();
        JSONArray conArgs2 = new JSONArray();
        conArgs2.add(3);
        singleCon2.element("condition_id", "000005up");
        singleCon2.element("args", conArgs2);
        JSONObject condition2 = new JSONObject();
        condition2.element("type", "single");
        condition2.element("condition1", singleCon2);

        JSONObject outerCon = new JSONObject();
        outerCon.element("type", "and");
        outerCon.element("condition1", condition);
        outerCon.element("condition2", condition2);

        JSONObject singleAct = new JSONObject();
        JSONArray actArgs = new JSONArray();
        actArgs.add(199);
        singleAct.element("action_id", "000005buy");
        singleAct.element("args", actArgs);
        JSONObject act = new JSONObject();
        act.element("type", "single");
        act.element("value", singleAct);

        JSONObject recipe = new JSONObject();
        recipe.element("condition", outerCon);
        recipe.element("action", act);

        JSONArray recipes = new JSONArray();
        recipes.add(recipe);

        JSONObject outmost = new JSONObject();
        outmost.element("recipes", recipes);
        outmost.element("stockCode", "\'sh601398\'");

        OperationResult<String> compiledResult = new RecipeImpl().compile(outmost.toString());
        System.out.println(compiledResult.success);
        System.out.println(compiledResult.getBundle());
    }

    // import
    public static void main2(String[] args)
    {
        List<String> imports = new LinkedList<>();
        imports.add("from zipline.api import symbol, order");
        imports.add("from zipline.api import  order");
        imports.add("import pandas");

        RecipeImpl impl = new RecipeImpl();
        System.out.println(impl.mergeImports(imports));
        System.out.println();
    }


    // parse Condition
    public static void main1(String[] args)
    {
        ConActService conActService = new ConActImpl();
        Collection<ConditionVO> conditionVOs = conActService.getRandomConditions(1);
        ConditionVO conditionVO = conditionVOs.iterator().next();

        RecipeImpl impl = new RecipeImpl();

        String def = conditionVO.definition;

        // replace args with params
        String stockCode = "\'sh601398\'";
        String temp = impl.replaceArgs(def, "stock", stockCode);
        List<String> params = impl.getParams(def);
        List<String> argsArray = new LinkedList<>();
        argsArray.add("998");

        assert params.size() == argsArray.size();

        for (int i = 0; i < argsArray.size(); i++)
        {
            String param = params.get(i);
            String arg = argsArray.get(i);

            temp = impl.replaceArgs(temp, param, arg);
        }

        // rename other vars to avoid conflicts
        temp = impl.renameVariables(temp, "con" + impl.conditionCount);
        ++impl.conditionCount;

        String compute = impl.extractPart(temp, "compute");
        String boolExpr = UtilityTools.superTrim(impl.extractPart(temp, "condition"));

        ConditionIR result = new ConditionIR(0, compute, boolExpr);
        System.out.println(result.toFinalRepresentation());
    }
}
