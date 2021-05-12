package com.huawei.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huawei.yygh.hosp.repository.DepartmentRepository;
import com.huawei.yygh.hosp.service.DepartmentService;
import com.huawei.yygh.model.hosp.Department;
import com.huawei.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author: Leslie
 * @create: 2021-05-11 16:52
 **/
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        //paramMap 转换department对象
        String paramStringObject = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramStringObject, Department.class);
        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());
        if (departmentExist != null) {
            BeanUtils.copyProperties(department,departmentExist,Department.class);
            departmentRepository.save(departmentExist);
        }else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        //创建Pageable对象，设置当前页和每页记录数，0是第一页
        Pageable pageable = PageRequest.of(page-1,limit);
        //创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department,matcher);
        Page<Department> pageAll = departmentRepository.findAll(example,pageable);
        return pageAll;
    }
}
