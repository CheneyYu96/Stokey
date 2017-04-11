package org.xeon.stockey.businessLogicService.strategyService;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xeon.stockey.util.OperationResult;

/**
 * 制作IF x THEN y 的Recipe，需要的方法
 * Created by Sissel on 2016/6/16.
 */
public interface RecipeService
{
    String extractPart(String origin, String partName);

    /**
     * 把json格式的List<RecipeVO>编译成python代码
     * @param json json格式的List<RecipeVO>
     * @return 编译后的python代码
     */
    //public OperationResult<String> compile(JSONObject json);

    @RequestMapping(method = RequestMethod.POST, value = "run")
    OperationResult<String> compile(@RequestBody String rawText);
}
