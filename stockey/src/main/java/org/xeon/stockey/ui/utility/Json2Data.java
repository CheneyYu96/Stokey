package org.xeon.stockey.ui.utility;

import net.sf.json.JSONObject;
import org.xeon.stockey.vo.JsonInfoVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by yuminchen on 16/5/31.
 */
public class Json2Data {
    public static List<JsonInfoVO> getJsonInfo(JSONObject jsonObject){
        List<JsonInfoVO> jsonInfoVOs = new ArrayList<>();
        //todo find the key to establish the vo
        Iterator iterator = jsonObject.keys();

        while (iterator.hasNext()){

            JsonInfoVO vo = new JsonInfoVO();
            String dateString = (String) iterator.next();

            JSONObject subObject = (JSONObject) jsonObject.get(dateString);
            double money = (double)subObject.get("portfolio_value");
            LocalDate date = DateUtil.parse(dateString.substring(0,10));

            vo.setDate(date);
            vo.setTotalMoney(money);
            jsonInfoVOs.add(vo);
        }

        return jsonInfoVOs;
    }


}
