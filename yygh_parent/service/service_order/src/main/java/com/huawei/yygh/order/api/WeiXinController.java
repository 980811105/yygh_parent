package com.huawei.yygh.order.api;

import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.order.service.PaymentService;
import com.huawei.yygh.order.service.WeiXinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-26 22:00
 **/
@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/order/weixin")
public class WeiXinController {
    @Autowired
    private WeiXinService weiXinService;

    @Autowired
    private PaymentService paymentService;

    @ApiOperation(value = "下单，生成支付二维码")
    @GetMapping("createNative/{orderId}")
    public Result createNative(@PathVariable Long orderId){
        Map map = weiXinService.createNative(orderId);
        return Result.ok(map);
    }

    @ApiOperation(value = "查询支付状态")
    @GetMapping("queryPayStatus/{orderId}")
    public Result queryPayStatus(@PathVariable Long orderId) {
        //调用微信接口实现支付状态的查询
        Map<String,String> resultMap = weiXinService.queryPayStatus(orderId);
        //判断支付状态
        if (null == resultMap){
            return Result.fail().message("支付失败");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))){
            //更改订单状态，处理支付结果
            String out_trade_no = resultMap.get("out_trade_no");
            paymentService.paySuccess(out_trade_no, resultMap);
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中");
    }
}
