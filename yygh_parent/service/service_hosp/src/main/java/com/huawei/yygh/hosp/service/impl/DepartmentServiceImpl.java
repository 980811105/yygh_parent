package com.huawei.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huawei.yygh.hosp.repository.DepartmentRepository;
import com.huawei.yygh.hosp.service.DepartmentService;
import com.huawei.yygh.model.hosp.Department;
import com.huawei.yygh.vo.hosp.DepartmentQueryVo;
import com.huawei.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (null != department) {
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //创建list集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();
        //根据医院编号，查询所有医院所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        //所有科室列表 departmentList
        List<Department> departmentsList = departmentRepository.findAll(example);
        //根据大科室编号 bigcode 分组，获取每个大科室下级子科室
        Map<String,List<Department>> departmentMap = departmentsList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //遍历map集合departmentMap
        for (Map.Entry<String,List<Department>> entry : departmentMap.entrySet()) {
            //大科室编号
            String bidcode = entry.getKey();
            //大科室编号对应全局数据
            List<Department> departmentList1 = entry.getValue();
            //封装大科室
            DepartmentVo departmentVo1 = new DepartmentVo();
            departmentVo1.setDepcode(bidcode);
            departmentVo1.setDepname(departmentList1.get(0).getBigname());

            //封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : departmentList1) {
                DepartmentVo departmentVo2 = new DepartmentVo();
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
                //封装到list集合
                children.add(departmentVo2);
            }
            //把小科室list集合放到大科室children里面
            departmentVo1.setChildren(children);
            //放到最终result里面
            result.add(departmentVo1);
        }
        return result;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }
        return null;
    }

    //根据医院编号和科室编号，得到科室对象
    @Override
    public Department getDepartment(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
    }
}


