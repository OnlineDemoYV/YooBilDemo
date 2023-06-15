package org.sdk.v1.pojo.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.sdk.v1.pojo.base.BaseGetDTO;

import java.security.interfaces.RSAPrivateKey;

/**
 * @author alen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OnlinePayReq extends BaseGetDTO {
    
    private Integer merchantId;
    private Integer businessId;
    private Integer feeId;
    private String clientId;
    private String timestamp = System.currentTimeMillis() + "";
    private String version = "2.0";
    private String currency = "VND";
    private String amount;
    private String name;
    private String userName;
    private String phoneNumber;
    private String orderNo;
    private String returnUrl;
    private String idNo;
    private String remark;
    private Integer serviceProvide;
    public String buildQuery(RSAPrivateKey rsaPrivateKey, String secretKey) {
        return super.buildReqQueryString(rsaPrivateKey, secretKey, this);
    }
}
