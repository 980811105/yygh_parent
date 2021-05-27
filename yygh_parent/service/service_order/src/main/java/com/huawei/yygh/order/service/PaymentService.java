package com.huawei.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.yygh.model.order.OrderInfo;
import com.huawei.yygh.model.order.PaymentInfo;

import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-26 22:13
 **/
public interface PaymentService extends IService<PaymentInfo> {

    void savePaymenyInfo(OrderInfo orderInfo, Integer status);

    void paySuccess(String out_trade_no, Map<String, String> resultMap);

    /**
     * 获取支付记录
     * @param orderId
     * @param paymentType
     * @return
     */
    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);

}
