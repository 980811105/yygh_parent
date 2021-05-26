package com.huawei.yygh.order.api;

import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.model.order.OrderInfo;
import com.huawei.yygh.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: Leslie
 * @create: 2021-05-24 16:06
 **/
@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {
    @Resource
    private OrderService orderService;

    @ApiOperation(value = "创建订单")
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result saveOrders(@PathVariable String scheduleId,
                              @PathVariable Long patientId){
        Long orderId = orderService.saveOrder(scheduleId,patientId);
        return Result.ok(orderId);
    }

    @ApiOperation(value = "根据订单id查询订单详情")
    @GetMapping("auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable String orderId){
        OrderInfo orderInfo =  orderService.getOrders(orderId);
        return Result.ok(orderInfo);
    }
}
