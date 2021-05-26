package com.huawei.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huawei.yygh.common.exception.YyghException;
import com.huawei.yygh.common.result.ResultCodeEnum;
import com.huawei.yygh.hosp.mapper.HospitalSetMapper;
import com.huawei.yygh.hosp.service.HospitalSetService;
import com.huawei.yygh.model.hosp.HospitalSet;
import com.huawei.yygh.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

/**
 * @author: Leslie
 * @create: 2021-04-25 11:42
 **/
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper,HospitalSet> implements HospitalSetService{

    //根据传递过来的医院编号，查询数据库签名
    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }

    //根据医院编号获取医院签名信息
    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;

    }
}
