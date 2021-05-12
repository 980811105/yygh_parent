package com.huawei.yygh.hosp.service;

import com.huawei.yygh.model.hosp.Hospital;

import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-10 13:56
 **/
public interface HospitalService {
    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);
}
