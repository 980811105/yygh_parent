package com.huawei.yygh.hosp.receive;

import com.huawei.common.rabbit.constant.MqConst;
import com.huawei.common.rabbit.service.RabbitService;
import com.huawei.yygh.hosp.service.ScheduleService;
import com.huawei.yygh.model.hosp.Schedule;
import com.huawei.yygh.vo.msm.MsmVo;
import com.huawei.yygh.vo.order.OrderMqVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author: Leslie
 * @create: 2021-05-24 18:50
 **/
@Component
public class HospitalReceive {
    @Resource
    private ScheduleService scheduleService;

    @Resource
    private RabbitService rabbitService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
        Schedule schedule;
        if(null != orderMqVo.getAvailableNumber()) {
            //下单成功更新预约数
            schedule = scheduleService.getScheduleById(orderMqVo.getScheduleId());
            schedule.setReservedNumber(orderMqVo.getReservedNumber());
            schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        } else {
            //取消预约更新预约数
            schedule = scheduleService.getScheduleById(orderMqVo.getScheduleId());
            int availableNumber = schedule.getAvailableNumber().intValue() + 1;
            schedule.setAvailableNumber(availableNumber);
        }
        scheduleService.update(schedule);
        //发送短信
        MsmVo msmVo = orderMqVo.getMsmVo();
        if(null != msmVo) {
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }
    }

}
