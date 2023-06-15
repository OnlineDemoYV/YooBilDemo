package org.sdk.v1.pojo;


import lombok.Data;
import lombok.experimental.Accessors;
import org.sdk.v1.utils.RsaUtils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author YooBil
 */
@Data
@Accessors(chain = true)
public class MerchantBaseCfg {
    
    private Integer merchantId;
    private Integer businessId;
    private String secretKey;
    private Integer feeId;
    private String baseUrl;
    
    private RSAPrivateKey merchantRsaPrivateKey;
    private RSAPublicKey merchantRsaPublicKey;
    private RSAPublicKey systemRsaPublicKey;
    
    public MerchantBaseCfg setMerchantRsaPrivateKey(String privateKeyPath) {
        merchantRsaPrivateKey = RsaUtils.loadPrivateKeyFromPath(privateKeyPath);
        return this;
    }
    
    public MerchantBaseCfg setMerchantRsaPublicKey(String publicKeyPath) {
        merchantRsaPublicKey = RsaUtils.loadPublicKeyFromPath(publicKeyPath);
        return this;
    }
    
    public MerchantBaseCfg setSystemRsaPublicKey(String systemRsaPublicKeyPath) {
        systemRsaPublicKey = RsaUtils.loadPublicKeyFromPath(systemRsaPublicKeyPath);
        return this;
    }
    
}
