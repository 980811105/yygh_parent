package com.huawei.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.yygh.model.hosp.HospitalSet;
import com.huawei.yygh.vo.order.SignInfoVo;

/**
 * @author: Leslie
 * @create: 2021-04-25 11:40
 **/
public interface HospitalSetService extends IService<HospitalSet> {
    //根据传递过来的医院编号，查询数据库签名
    String getSignKey(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}

