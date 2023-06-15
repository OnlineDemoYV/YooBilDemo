package org.sdk.v1.pojo.req.va;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.sdk.v1.pojo.base.BaseGetDTO;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

/**
 * @author alen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryVaTransactionPage extends BaseGetDTO {
    
    private Integer merchantId;
    private Integer businessId;
    private String orderNo;
    private String timestamp;
    private String version;
    private String tradeNo;
    private Integer pageSize;
    private Integer pageNum;
    private String startTime;
    private String endTime;
    
    public String buildQuery(RSAPrivateKey rsaPrivateKey, String secretKey) {
        return super.buildReqQueryString(rsaPrivateKey, secretKey, this);
    }
    
}
