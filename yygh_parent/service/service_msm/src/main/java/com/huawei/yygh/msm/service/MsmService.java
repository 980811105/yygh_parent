package com.huawei.yygh.msm.service;

import com.huawei.yygh.vo.msm.MsmVo;

/**
 * @author: Leslie
 * @create: 2021-05-22 10:22
 **/
public interface MsmService {
    //发送手机验证码
    boolean send(String phone,String code);

    //mq使用发送短信
    boolean send(MsmVo msmVo);
}
