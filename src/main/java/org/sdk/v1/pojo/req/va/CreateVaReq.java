package org.sdk.v1.pojo.req.va;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sdk.v1.pojo.base.BasePostDTO;

/**
 * @author alen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateVaReq extends BasePostDTO {
    
    private Integer merchantId;
    private Integer businessId;
    private Integer feeId;
    private String clientId;
    private String timestamp = System.currentTimeMillis() + "";
    private String version = "2.0";
    private String currency = "VND";
    private String amount;
    private String orderNo;
    private String expireDate;
    private String returnUrl;
    private String accountBase;
    private String phoneNumber;
    private String userName;
    private String idNo;
}
