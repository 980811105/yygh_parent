package com.huawei.yygh.hosp.repository;

import com.huawei.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: Leslie
 * @create: 2021-05-10 13:55
 **/
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    //判断数据是否存在
    Hospital getHospitalByHoscode(String hoscode);
}
