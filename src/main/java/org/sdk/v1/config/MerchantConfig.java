package org.sdk.v1.config;

/**
 * @author YooBil
 */
public class MerchantConfig {
    /**
     * Path of merchant private key
     */
    public static final String privateKeyPath = "./secrets/merchant-private.pem";
    
    /**
     * Path of merchant public key
     */
    public static final String publicKeyPath = "./secrets/merchant-public.pem";
    
    /**
     * Path of system public key, for verifying signature
     */
    public static final String systemRsaPublicKeyPath = "./secrets/funpay-public.pem";
    
    /**
     * Secret key
     */
    public static final String secretKey = "";
    
    /**
     * Default request address
     */
    public static final String baseUrl = "https://uat.yoobil.com/yoobil";

    /**
     * Merchant info on Yoobil
     */
    public static final Integer merchantId = 0;
    public static final Integer businessId = 0;
    public static final Integer feeId = 0;
    
}
