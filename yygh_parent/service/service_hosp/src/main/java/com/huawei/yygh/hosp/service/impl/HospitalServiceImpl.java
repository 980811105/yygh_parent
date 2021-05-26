package com.huawei.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huawei.yygh.cmn.client.DictFeignClient;
import com.huawei.yygh.hosp.repository.HospitalRepository;
import com.huawei.yygh.hosp.service.HospitalService;
import com.huawei.yygh.model.hosp.Hospital;
import com.huawei.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-10 13:57
 **/
@Service
public class HospitalServiceImpl implements HospitalService {
    @Resource
    private HospitalRepository hospitalRepository;

    @Resource
    private DictFeignClient dictFeignClient;
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
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //创建Pageable对象，设置当前页和每页记录数，0是第一页
        Pageable pageable = PageRequest.of(page-1,limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //创建Example对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        Example<Hospital> example = Example.of(hospital,matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        pages.getContent().stream().forEach(item -> {
            this.setHospitalHosType(item);
        });
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if (status.intValue() == 0 || status.intValue() == 1){
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Map<String,Object> getHospById(String id) {
        Map<String,Object> result = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        result.put("hospital",hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    //根据医院编号查询医院名称
    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital != null) {
            return hospital.getHosname();
        }
        return "";
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    //根据医院编号获取医院预约挂号详情信息
    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        result.put("hospital",hospital);
        //预约规则
        result.put("bookingRule",hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    //获取查询list集合，遍历进行医院等级封装
    private Hospital setHospitalHosType(Hospital hospital) {
        //根据dictCode和value获取医院等级名称
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        //查询省、市、地区
        String provinceString = dictFeignClient.getName( hospital.getProvinceCode());
        String cityString = dictFeignClient.getName( hospital.getCityCode());
        String districtString = dictFeignClient.getName( hospital.getDistrictCode());
        hospital.getParam().put("fullAddress",provinceString+cityString+districtString);
        hospital.getParam().put("hostypeString",hostypeString);
        return hospital;
    }
}
