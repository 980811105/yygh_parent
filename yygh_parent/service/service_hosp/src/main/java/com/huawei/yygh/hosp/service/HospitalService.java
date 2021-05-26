package com.huawei.yygh.hosp.service;

import com.huawei.yygh.model.hosp.Hospital;
import com.huawei.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-10 13:56
 **/
public interface HospitalService {
    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String,Object> getHospById(String id);

    String getHospName(String hoscode);

    List<Hospital> findByHosname(String hosname);

    Map<String, Object> item(String hoscode);
}
