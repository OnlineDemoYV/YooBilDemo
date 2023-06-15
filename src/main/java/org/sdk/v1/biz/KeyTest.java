package org.sdk.v1.biz;

import org.junit.Test;
import org.sdk.v1.utils.RsaUtils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author YooBil
 */
public class KeyTest {
    
    @Test
    public void keyTest() throws Exception {
        
        RSAPrivateKey privateKey = RsaUtils.newRsaPrivateKey("");
        RSAPublicKey publicKey = RsaUtils.newRsaPublicKey("");
        
    }
}
