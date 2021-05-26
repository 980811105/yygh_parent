package com.huawei.yygh.hosp.service;


import com.huawei.yygh.model.hosp.Department;
import com.huawei.yygh.vo.hosp.DepartmentQueryVo;
import com.huawei.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-11 16:52
 **/
public interface DepartmentService {
    //上传科室信息
    void save(Map<String, Object> paramMap);
    //查询科室信息
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}
