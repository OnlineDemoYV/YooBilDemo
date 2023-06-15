package org.sdk.v1.pojo.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.sdk.v1.utils.PlaintextUtils;
import org.sdk.v1.utils.RsaUtils;

import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author YooBil
 */
@Data
public class BaseGetDTO {
    
    public String buildReqQueryString(RSAPrivateKey rsaPrivateKey, String secretKey, Object bean) {
        
        Map<String, Object> paramMap = BeanUtil.beanToMap(bean).entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, e -> StrUtil.isBlankIfStr(e.getValue()) ? "" : e.getValue().toString())
        );
        
        //1. get plain text for sign
        String text = PlaintextUtils.getPlaintext(paramMap, secretKey, true);
        //Sign
        String sign = RsaUtils.signWithPrivateKey(text.getBytes(), rsaPrivateKey);
        
        //2. append sign to parameters
        paramMap.put("sign", sign);
        
        //3. get new plain text
        String queryString = PlaintextUtils.getPlaintext(paramMap, "", true);
        
        //4. Url Safe Base64
        return Base64.encode(queryString);
    }
}
