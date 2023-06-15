package org.sdk.v1.biz;

import cn.hutool.http.HttpResponse;
import org.sdk.v1.pojo.req.OnlinePayReq;
import org.sdk.v1.pojo.req.QueryOnlineOrderReq;
import org.sdk.v1.pojo.req.QueryTransferOrderReq;
import org.sdk.v1.pojo.req.TransferReq;
import org.sdk.v1.pojo.req.va.CreateVaReq;
import org.sdk.v1.pojo.req.va.QueryVaDetailReq;
import org.sdk.v1.pojo.req.va.QueryVaTransaction;
import org.sdk.v1.pojo.req.va.QueryVaTransactionPage;


/**
 * @author alen
 */
public interface ApiClient {
    /**
     * Create Virtual Account
     *
     * @param createVaReq createVaReq
     * @return HttpResponse HttpResponse
     */
    HttpResponse createVa(CreateVaReq createVaReq);
    
    /**
     * payout
     *
     * @param transferReq transferReq
     * @return HttpResponse HttpResponse
     */
    HttpResponse payoutTransfer(TransferReq transferReq);
    
    /**
     * payout-online
     *
     * @param onlinePayReq transferReq
     * @return HttpResponse HttpResponse
     */
    String payoutOnline(OnlinePayReq onlinePayReq);
    
    /**
     * Query collection orders
     *
     * @param queryOnlineOrder queryOnlineOrder
     * @return HttpResponse HttpResponse
     */
    HttpResponse queryOnlineOrder(QueryOnlineOrderReq queryOnlineOrder);
    
    /**
     * Query disbursement orders
     *
     * @param queryTransferOrder queryTransferOrder
     * @return HttpResponse HttpResponse
     */
    HttpResponse queryTransferOrder(QueryTransferOrderReq queryTransferOrder);
    
    
    /**
     * Query VA info
     *
     * @param queryTransferOrder queryTransferOrder
     * @return HttpResponse
     */
    HttpResponse queryVa(QueryVaDetailReq queryTransferOrder);
    
    /**
     * Query VA transactions
     *
     * @param queryVaTransaction queryVaTransaction
     * @return HttpResponse
     */
    HttpResponse queryTransaction(QueryVaTransaction queryVaTransaction);
    
    /**
     * Query VA transaction record
     *
     * @param queryVaTransactionPage queryVaTransactionPage
     * @return HttpResponse
     */
    HttpResponse queryTransactionList(QueryVaTransactionPage queryVaTransactionPage);
    
}
