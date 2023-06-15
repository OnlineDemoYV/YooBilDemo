package org.sdk.v1.biz;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.sdk.v1.pojo.req.OnlinePayReq;
import org.sdk.v1.pojo.req.QueryOnlineOrderReq;
import org.sdk.v1.pojo.req.QueryTransferOrderReq;
import org.sdk.v1.pojo.req.TransferReq;
import org.sdk.v1.pojo.req.va.CreateVaReq;
import org.sdk.v1.pojo.req.va.QueryVaDetailReq;
import org.sdk.v1.pojo.req.va.QueryVaTransactionPage;


/**
 * API DEMO for reference only
 * @author YooBil
 */
@Slf4j
public class ApiBizDemoTest extends BaseMerchantInfo {
    
    private final ApiClientImpl apiClient = new ApiClientImpl(getMerchantCfg());
    
    
    @Test
    public void queryTradeTest() {
        
        
        QueryOnlineOrderReq queryOnlineOrder = new QueryOnlineOrderReq();
        
        queryOnlineOrder
                .setTimestamp(String.valueOf(System.currentTimeMillis()))
                .setOrderNo("166519950965612313")
                .setVersion("2.0");
        
    }
    
    
    @Test
    public void onlinePayNewCore() {
        
        long currentTimeStamp = System.currentTimeMillis();
        
        String orderNo = currentTimeStamp + UUID.fastUUID().toString().substring(0, 5);
        
        OnlinePayReq onlinePayReq = new OnlinePayReq();
        
        onlinePayReq.setAmount("50000")
                .setClientId("alen-test")
                .setCurrency("VND")
                .setFeeId(13)
                .setIdNo("987654321")
                .setName("testMerchant")
                .setOrderNo(orderNo)
                .setPhoneNumber("21678467812")
                .setRemark("987654321")
                .setReturnUrl("")
                .setServiceProvide(0)
                .setTimestamp(String.valueOf(currentTimeStamp))
                .setUserName("alenTest")
                .setVersion("2.0");
        
        
        String checkOutUrl = apiClient.payoutOnline(onlinePayReq);
        
        log.info("checkOutUrl:{}", checkOutUrl);
        
    }
    
    @Test
    public void transfer() {
        TransferReq transferReq = new TransferReq();
        
        transferReq.setTimestamp(System.currentTimeMillis() + "");
        transferReq.setAmount("50000");
        transferReq.setCurrency("VND");
        transferReq.setVersion("2.0");
        transferReq.setOrderNo(System.currentTimeMillis() + UUID.fastUUID().toString().substring(0, 5));
        transferReq.setReturnUrl("");
        transferReq.setAccountType(0);
        transferReq.setAccountNo("100100162348");
        transferReq.setAccountName("CHF F F F");
        transferReq.setBankNo(39);
        transferReq.setBankLocation("vn");
        
        transferReq.setClientId("");
        transferReq.setRemark("Test200");
        
        HttpResponse httpResponse = apiClient.payoutTransfer(transferReq);
        
    }
    
    @Test
    public void queryTransferTradeTest() {
        
        
        QueryTransferOrderReq queryTransferOrder = new QueryTransferOrderReq();
        
        queryTransferOrder
                .setTimestamp(String.valueOf(System.currentTimeMillis()))
                .setOrderNo("1679300594746c26e9")
                .setVersion("2.0");
        
        HttpResponse httpResponse = apiClient.queryTransferOrder(queryTransferOrder);
        
    }
    
    @Test
    public void virtualCardCreate() {
        String accBase = "1923723474114";
        
        CreateVaReq createVaReq = new CreateVaReq();
        
        createVaReq.setTimestamp(System.currentTimeMillis() + "");
        createVaReq.setAmount("50000");
        createVaReq.setCurrency("VND");
        createVaReq.setVersion("2.0");
        createVaReq.setOrderNo(System.currentTimeMillis() + UUID.fastUUID().toString().substring(0, 5));
        createVaReq.setExpireDate("20220701");
        createVaReq.setReturnUrl("https://sz.lailaiche.com/vibill/callback/funpay/va");
        createVaReq.setAccountBase(accBase);
        createVaReq.setPhoneNumber("1741234444");
        createVaReq.setUserName("BS Vi");
        createVaReq.setIdNo("1231412412");
        createVaReq.setClientId("alen-test");
        
        HttpResponse httpResponse = apiClient.createVa(createVaReq);
    }
    
    @Test
    public void virtualCardQuery() {
        QueryVaDetailReq queryVaDetailReq = new QueryVaDetailReq();
        queryVaDetailReq.setVersion("2.0");
        queryVaDetailReq.setTimestamp(System.currentTimeMillis() + "");
        queryVaDetailReq.setOrderNo("16784440759629ebf6");
        
        HttpResponse httpResponse = apiClient.queryVa(queryVaDetailReq);
        
    }
    
    @Test
    public void virtualCardQueryTransaction() {
        
        
        QueryVaTransactionPage queryVaTransactionPage = new QueryVaTransactionPage();
        queryVaTransactionPage.setVersion("2.0");
        queryVaTransactionPage.setTimestamp(System.currentTimeMillis() + "");
        queryVaTransactionPage.setOrderNo("16784440759629ebf6");
        queryVaTransactionPage.setPageNum(1);
        queryVaTransactionPage.setPageSize(10);
        queryVaTransactionPage.setStartTime("2023-03-04 12:00:00");
        queryVaTransactionPage.setEndTime("2023-03-28 12:00:00");
        
        HttpResponse httpResponse = apiClient.queryTransactionList(queryVaTransactionPage);
        
    }
    
    
}
