package com.huawei.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.yygh.model.order.OrderInfo;
import com.huawei.yygh.vo.order.OrderCountQueryVo;
import com.huawei.yygh.vo.order.OrderQueryVo;

import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-24 16:04
 **/
public interface OrderService extends IService<OrderInfo> {
    Long saveOrder(String scheduleId, Long patientId);

    OrderInfo getOrders(String orderId);

    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    Boolean cancelOrder(Long orderId);

    void patientTips();

    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);
}
