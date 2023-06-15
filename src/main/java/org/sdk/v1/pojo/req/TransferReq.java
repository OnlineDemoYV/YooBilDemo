package org.sdk.v1.pojo.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.sdk.v1.pojo.base.BasePostDTO;

/**
 * @author alen
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TransferReq extends BasePostDTO {
    
    Integer merchantId;
    Integer businessId;
    Integer feeId;
    String clientId;
    String timestamp = System.currentTimeMillis() + "";
    String version = "2.0";
    String currency = "VND";
    String amount;
    String accountName;
    Integer accountType;
    String accountNo;
    String bankLocation;
    String orderNo;
    Integer bankNo;
    
    String returnUrl;
    String remark;
    
}
