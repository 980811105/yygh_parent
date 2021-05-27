package com.huawei.yygh.task.scheduletask;

import com.huawei.common.rabbit.constant.MqConst;
import com.huawei.common.rabbit.service.RabbitService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: Leslie
 * @create: 2021-05-27 02:05
 **/
@Component
@EnableScheduling
public class ScheduleTask {
    @Resource
    private RabbitService rabbitService;

    /**
     * 每天8点执行 提醒就诊
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0/30 * * * * ?")
    public void task1() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK,
                MqConst.ROUTING_TASK_8, "");
    }

}
