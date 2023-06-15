package org.sdk.v1.pojo.req.va;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.sdk.v1.pojo.base.BaseGetDTO;

import java.security.interfaces.RSAPrivateKey;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryVaDetailReq extends BaseGetDTO {
    Integer merchantId;
    Integer businessId;
    String orderNo;
    String timestamp;
    String version;
    
    private String accountNo;
    
    public String buildQuery(RSAPrivateKey rsaPrivateKey, String secretKey) {
        return super.buildReqQueryString(rsaPrivateKey, secretKey, this);
    }
}
