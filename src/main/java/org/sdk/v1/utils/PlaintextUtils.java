package org.sdk.v1.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.val;

import java.util.*;

/**
 * @author YooBil
 */
public class PlaintextUtils {
    public static String getPlaintext(Map<String, Object> map, String key, boolean hasNull) {
        //sort the map by key
        val paramList = new ArrayList<>(map.keySet());
        paramList.sort(Comparator.naturalOrder());
        // Get a string
        StringJoiner sj = new StringJoiner("&");
        
        if (hasNull) {
            paramList.forEach(k -> {
                String var;
                if (map.get(k) == null) {
                    var = "null";
                } else {
                    var = map.get(k).toString();
                }
                sj.add(k + "=" + var);
            });
        } else {
            paramList.forEach(k -> sj.add(k + "=" + map.get(k)));
        }
        
        if (StrUtil.isNotBlank(key)) {
            return sj + key;
        }
        return sj.toString();
    }
    
    
    public static String getPlaintext(JSONObject jsonObject, String key, boolean hasNull) {
        jsonObject = jsonObject.getJSONObject("result");
        
        Map<String, Object> resultMap = new HashMap<>(16);
        
        /*
         * covert result to map
         */
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue().toString());
        }
        
        /*
         * skip the parameter sign
         * skip other parameters that do not sign
         */
        resultMap.remove("sign");
        resultMap.remove("transactions");
        
        return getPlaintext(resultMap, key, hasNull);
    }
}
