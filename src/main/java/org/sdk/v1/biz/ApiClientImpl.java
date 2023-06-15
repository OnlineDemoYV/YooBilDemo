package org.sdk.v1.biz;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.sdk.v1.exception.BusinessException;
import org.sdk.v1.pojo.MerchantBaseCfg;
import org.sdk.v1.pojo.req.OnlinePayReq;
import org.sdk.v1.pojo.req.QueryOnlineOrderReq;
import org.sdk.v1.pojo.req.QueryTransferOrderReq;
import org.sdk.v1.pojo.req.TransferReq;
import org.sdk.v1.pojo.req.va.CreateVaReq;
import org.sdk.v1.pojo.req.va.QueryVaDetailReq;
import org.sdk.v1.pojo.req.va.QueryVaTransaction;
import org.sdk.v1.pojo.req.va.QueryVaTransactionPage;
import org.sdk.v1.utils.PlaintextUtils;
import org.sdk.v1.utils.RsaUtils;


/**
 * @author YooBil
 */
@Slf4j
@Setter
public class ApiClientImpl implements ApiClient {
    
    private MerchantBaseCfg merchantBaseCfg;
    
    public ApiClientImpl(MerchantBaseCfg merchantBaseCfg) {
        this.merchantBaseCfg = merchantBaseCfg;
    }
    
    /**
     * Create Virtual Account
     *
     * @param var var
     * @return HttpResponse HttpResponse
     */
    @Override
    public HttpResponse createVa(CreateVaReq var) {
        
        var.setMerchantId(merchantBaseCfg.getMerchantId());
        var.setBusinessId(merchantBaseCfg.getBusinessId());
        var.setFeeId(merchantBaseCfg.getFeeId());
        var.apiSign(merchantBaseCfg.getMerchantRsaPrivateKey(), merchantBaseCfg.getSecretKey(), var);
        
        String url = merchantBaseCfg.getBaseUrl() + "/trade/vn/virtual/create";
        
        log.info("Request address： {}", url);
        log.info("request parameters： {}", JSON.toJSONString(var));
        
        HttpResponse response = HttpRequest.post(url)
                .setConnectionTimeout(30000).setReadTimeout(30000)
                .body(JSON.toJSONString(var)).execute();
        
        log.info("Return result：{}", response.body());
        
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        
        if (!"10000".equals(jsonObject.getString("code"))) {
            throw new BusinessException(jsonObject.getString("msg"));
        }
        
        String plaintext = PlaintextUtils.getPlaintext(jsonObject, merchantBaseCfg.getSecretKey(), true);
        
        log.info(plaintext);
        
        String signText = JSONObject.parseObject(response.body()).getJSONObject("result").getString("sign");
        
        
        if (!RsaUtils.verifyWithPublicKey(plaintext.getBytes(), signText, merchantBaseCfg.getSystemRsaPublicKey())) {
            throw new BusinessException("verify response sign failed");
        } else {
            log.info("Signature matches");
        }
        
        log.info(response.body());
        
        
        return response;
    }
    
    /**
     * payout
     *
     * @param var var
     * @return HttpResponse HttpResponse
     */
    @Override
    public HttpResponse payoutTransfer(TransferReq var) {
        
        var.setMerchantId(merchantBaseCfg.getMerchantId());
        var.setBusinessId(merchantBaseCfg.getBusinessId());
        var.setFeeId(merchantBaseCfg.getFeeId());
        
        
        var.apiSign(merchantBaseCfg.getMerchantRsaPrivateKey(), merchantBaseCfg.getSecretKey(), var);
        
        log.info("Request address {}", merchantBaseCfg.getBaseUrl() + "/trade/vn/transfer/pay");
        
        HttpResponse response = HttpRequest.post(merchantBaseCfg.getBaseUrl() + "/trade/vn/transfer/pay")
                .setConnectionTimeout(30000).setReadTimeout(30000)
                .body(JSON.toJSONString(var)).execute();
        
        log.info(response.toString());
        
        JSONObject jsonObject = JSON.parseObject(response.body());
        
        if (!"10000".equals(jsonObject.getString("code"))) {
            throw new BusinessException(jsonObject.getString("msg"));
        }
        
        String plaintext = PlaintextUtils.getPlaintext(jsonObject, merchantBaseCfg.getSecretKey(), true);
        
        log.info(plaintext);
        
        String signText = JSONObject.parseObject(response.body()).getJSONObject("result").getString("sign");
        
        if (!RsaUtils.verifyWithPublicKey(plaintext.getBytes(), signText, merchantBaseCfg.getSystemRsaPublicKey())) {
            throw new BusinessException("transferMoney verify response sign failed");
        }
        
        return response;
    }
    
    /**
     * payout-online
     *
     * @param var transferReq
     * @return HttpResponse HttpResponse
     */
    @Override
    public String payoutOnline(OnlinePayReq var) {
        
        var.setMerchantId(merchantBaseCfg.getMerchantId());
        var.setBusinessId(merchantBaseCfg.getBusinessId());
        var.setFeeId(merchantBaseCfg.getFeeId());
        
        String queryParam = var.buildQuery(merchantBaseCfg.getMerchantRsaPrivateKey(), merchantBaseCfg.getSecretKey());
        
        log.info(merchantBaseCfg.getBaseUrl() + "/trade/vn/receipt/online/pay" + "?param=" + queryParam);
        HttpResponse httpResponse = HttpRequest.get(merchantBaseCfg.getBaseUrl() + "/trade/vn/receipt/online/pay" + "?param=" + queryParam).execute();
        
        //if the http code is 301 or 302 relocate/forward
        if (httpResponse.getStatus() == 301 || httpResponse.getStatus() == 302) {
            String location = httpResponse.header("Location");
            return location;
        }
        return "error";
    }
    
    /**
     * Query orders
     *
     * @param var var
     * @return HttpResponse HttpResponse
     */
    @Override
    public HttpResponse queryOnlineOrder(QueryOnlineOrderReq var) {
        
        var.setMerchantId(merchantBaseCfg.getMerchantId());
        var.setBusinessId(merchantBaseCfg.getBusinessId());
        
        String queryParam = var.buildQuery(merchantBaseCfg.getMerchantRsaPrivateKey(), merchantBaseCfg.getSecretKey());
        
        HttpResponse httpResponse = HttpRequest.get(merchantBaseCfg.getBaseUrl() + "/trade/india/online/check" + "?param=" + queryParam)
                .execute();
        
        String plaintext = PlaintextUtils.getPlaintext(JSONObject.parseObject(httpResponse.body()), merchantBaseCfg.getSecretKey(), true);
        String signText = JSONObject.parseObject(httpResponse.body()).getJSONObject("result").getString("sign");
        
        if (!RsaUtils.verifyWithPublicKey(plaintext.getBytes(), signText, merchantBaseCfg.getSystemRsaPublicKey())) {
            throw new BusinessException(" verify response sign failed");
        }
        return httpResponse;
    }
    
    /**
     * Query disbursement orders
     *
     * @param var var
     * @return HttpResponse HttpResponse
     */
    @Override
    public HttpResponse queryTransferOrder(QueryTransferOrderReq var) {
        
        var.setMerchantId(merchantBaseCfg.getMerchantId());
        var.setBusinessId(merchantBaseCfg.getBusinessId());
        
        
        String queryParam = var.buildQuery(merchantBaseCfg.getMerchantRsaPrivateKey(), merchantBaseCfg.getSecretKey());
        
        HttpResponse httpResponse = HttpRequest.get(merchantBaseCfg.getBaseUrl() + "/trade/vn/transfer/check" + "?param=" + queryParam)
                .execute();
        
        log.info(httpResponse.body());
        
        String plaintext = PlaintextUtils.getPlaintext(JSONObject.parseObject(httpResponse.body()), merchantBaseCfg.getSecretKey(), true);
        log.info(plaintext);
        
        String signText = JSONObject.parseObject(httpResponse.body()).getJSONObject("result").getString("sign");
        
        if (!RsaUtils.verifyWithPublicKey(plaintext.getBytes(), signText, merchantBaseCfg.getSystemRsaPublicKey())) {
            throw new BusinessException(" verify response sign failed");
        }
        
        return httpResponse;
    }
    
    /**
     * Query Virtual Account Info
     *
     * @param var queryTransferOrder
     * @return HttpResponse
     */
    @Override
    public HttpResponse queryVa(QueryVaDetailReq var) {
        var.setMerchantId(merchantBaseCfg.getMerchantId());
        var.setBusinessId(merchantBaseCfg.getBusinessId());
        
        log.info("Request Address {}", merchantBaseCfg.getBaseUrl() + "/trade/vn/virtual/queryVc");
        
        log.info("Request parameters {}", JSON.toJSONString(var));
        
        
        String queryParam = var.buildQuery(merchantBaseCfg.getMerchantRsaPrivateKey(), merchantBaseCfg.getSecretKey());
        
        
        HttpResponse httpResponse = HttpRequest.get(merchantBaseCfg.getBaseUrl() + "/trade/vn/virtual/queryVc" + "?param=" + queryParam)
                .execute();
        
        log.info(httpResponse.body());
        
        String plaintext = PlaintextUtils.getPlaintext(JSONObject.parseObject(httpResponse.body()), merchantBaseCfg.getSecretKey(), true);
        log.info(plaintext);
        
        String signText = JSONObject.parseObject(httpResponse.body()).getJSONObject("result").getString("sign");
        
        if (!RsaUtils.verifyWithPublicKey(plaintext.getBytes(), signText, merchantBaseCfg.getSystemRsaPublicKey())) {
            throw new BusinessException(" verify response sign failed");
        }
        
        return httpResponse;
    }
    
    /**
     * Query transactions of VA
     *
     * @param var queryVaTransaction
     * @return HttpResponse
     */
    @Override
    public HttpResponse queryTransaction(QueryVaTransaction var) {
        var.setMerchantId(merchantBaseCfg.getMerchantId());
        var.setBusinessId(merchantBaseCfg.getBusinessId());
        
        
        String queryParam = var.buildQuery(merchantBaseCfg.getMerchantRsaPrivateKey(), merchantBaseCfg.getSecretKey());
        
        HttpResponse httpResponse = HttpRequest.get(merchantBaseCfg.getBaseUrl() + "/trade/vn/virtual/check" + "?param=" + queryParam)
                .execute();
        
        log.info(httpResponse.body());
        
        String plaintext = PlaintextUtils.getPlaintext(JSONObject.parseObject(httpResponse.body()), merchantBaseCfg.getSecretKey(), true);
        log.info(plaintext);
        
        String signText = JSONObject.parseObject(httpResponse.body()).getJSONObject("result").getString("sign");
        
        if (!RsaUtils.verifyWithPublicKey(plaintext.getBytes(), signText, merchantBaseCfg.getSystemRsaPublicKey())) {
            throw new BusinessException(" verify response sign failed");
        }
        
        return httpResponse;
    }
    
    /**
     * Query transaction list of VA
     *
     * @param var queryVaTransactionPage
     * @return HttpResponse
     */
    @Override
    public HttpResponse queryTransactionList(QueryVaTransactionPage var) {
        var.setMerchantId(merchantBaseCfg.getMerchantId());
        var.setBusinessId(merchantBaseCfg.getBusinessId());
        
        log.info("Request address {}", merchantBaseCfg.getBaseUrl() + "/trade/vn/virtual/query");
        log.info("Query virtual card transaction records by page: {}", JSON.toJSONString(var));
        
        String queryParam = var.buildQuery(merchantBaseCfg.getMerchantRsaPrivateKey(), merchantBaseCfg.getSecretKey());
        
        HttpResponse httpResponse = HttpRequest.get(merchantBaseCfg.getBaseUrl() + "/trade/vn/virtual/query" + "?param=" + queryParam)
                .execute();
        
        log.info("Return data {}", httpResponse.body());
        
        
        String plaintext = PlaintextUtils.getPlaintext(JSONObject.parseObject(httpResponse.body()), merchantBaseCfg.getSecretKey(), true);
        
        String signText = JSONObject.parseObject(httpResponse.body()).getJSONObject("result").getString("sign");
        
        if (!RsaUtils.verifyWithPublicKey(plaintext.getBytes(), signText, merchantBaseCfg.getSystemRsaPublicKey())) {
            log.info("Sign plaintext ： {}", plaintext);
            throw new BusinessException(" verify response sign failed");
        }
        
        return httpResponse;
    }
}
