package org.sdk.v1.pojo.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.sdk.v1.utils.PlaintextUtils;
import org.sdk.v1.utils.RsaUtils;

import java.io.Serializable;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * rsa签名基础dto
 *
 * @author alen
 */
@Data
@Accessors(chain = true)
public class BasePostDTO implements Serializable {
    
    /**
     * sign
     */
    private String sign;
    
    
    /**
     * 对外加密 有空值
     *
     * @param rsaPrivateKey rsa密钥
     * @param internalKey   INTERNAL_KEY
     */
    public void apiSign(RSAPrivateKey rsaPrivateKey, String internalKey, Object bean) {
        
        Map<String, Object> result = BeanUtil.beanToMap(bean).entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, e -> StrUtil.isBlankIfStr(e.getValue()) ? "" : e.getValue().toString())
        );
        result.remove("sign");
        
        String text = PlaintextUtils.getPlaintext(result, internalKey, true);
 
        this.sign = RsaUtils.signWithPrivateKey(text.getBytes(), rsaPrivateKey);
    }
    
}
