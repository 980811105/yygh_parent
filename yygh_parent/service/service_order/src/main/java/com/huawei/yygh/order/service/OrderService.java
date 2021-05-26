package com.huawei.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.yygh.model.order.OrderInfo;

/**
 * @author: Leslie
 * @create: 2021-05-24 16:04
 **/
public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);

    OrderInfo getOrders(String orderId);
}
