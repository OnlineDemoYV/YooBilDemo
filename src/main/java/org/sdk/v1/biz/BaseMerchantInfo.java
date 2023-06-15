package org.sdk.v1.biz;

import org.sdk.v1.config.MerchantConfig;
import lombok.Data;
import org.sdk.v1.pojo.MerchantBaseCfg;

/**
 * @author YooBil
 */

@Data
public class BaseMerchantInfo {
    
    public MerchantBaseCfg getMerchantCfg() {
        MerchantBaseCfg merchantBaseCfg = new MerchantBaseCfg();
        //Load private key
        merchantBaseCfg.setMerchantRsaPrivateKey(MerchantConfig.privateKeyPath)
                .setMerchantRsaPublicKey(MerchantConfig.publicKeyPath)
                .setSystemRsaPublicKey(MerchantConfig.systemRsaPublicKeyPath)
                .setSecretKey(MerchantConfig.secretKey);
        //Configure merchant info
        merchantBaseCfg.setMerchantId(MerchantConfig.merchantId).setBusinessId(MerchantConfig.businessId).setFeeId(MerchantConfig.feeId)
                .setBaseUrl(MerchantConfig.baseUrl);
        
        return merchantBaseCfg;
    }
}
