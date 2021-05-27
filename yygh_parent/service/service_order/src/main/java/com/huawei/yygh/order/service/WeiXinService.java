package com.huawei.yygh.order.service;

import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-26 22:01
 **/
public interface WeiXinService{
    Map createNative(Long orderId);

    Map<String, String> queryPayStatus(Long orderId);

    /***
     * 退款
     * @param orderId
     * @return
     */
    Boolean refund(Long orderId);

}
