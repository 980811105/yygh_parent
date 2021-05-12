package com.huawei.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huawei.yygh.hosp.repository.HospitalRepository;
import com.huawei.yygh.hosp.service.HospitalService;
import com.huawei.yygh.model.hosp.Hospital;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-10 13:57
 **/
@Service
public class HospitalServiceImpl implements HospitalService {
    @Resource
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        //把参数map集合转换对象Hospital
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap),Hospital.class);

        //判断是否存在数据
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        //如果存在，进行更新
        if (null != hospitalExist) {
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
        }else {
            //如果不存在，进行添加
            //0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
        }
        hospital.setUpdateTime(new Date());
        hospital.setIsDeleted(0);
        hospitalRepository.save(hospital);
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }
}
