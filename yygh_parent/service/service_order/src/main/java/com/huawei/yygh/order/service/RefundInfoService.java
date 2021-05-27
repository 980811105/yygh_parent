package com.huawei.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.yygh.model.order.PaymentInfo;
import com.huawei.yygh.model.order.RefundInfo;

/**
 * @author: Leslie
 * @create: 2021-05-27 00:56
 **/
public interface RefundInfoService extends IService<RefundInfo> {
    /**
     * 保存退款记录
     * @param paymentInfo
     */
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);

}
