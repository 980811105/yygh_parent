package com.huawei.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huawei.yygh.cmn.listener.DictListener;
import com.huawei.yygh.cmn.mapper.DictMapper;
import com.huawei.yygh.cmn.service.DictService;
import com.huawei.yygh.model.cmn.Dict;
import com.huawei.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Leslie
 * @create: 2021-04-25 11:42
 **/
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private DictMapper dictMapper;

    //根据数据id查询子数据列表
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        //向list集合每个dict对象中设置hasChildren
        for (Dict dict:dictList) {
/*            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);*/
            dict.setHasChildren(this.isChildren(dict.getId()));
        }
        return dictList;
    }

    //判断id下面是否有子节点
    private boolean isChildren(Long id) {
/*        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        // 0>0    1>0
        return count>0;*/
        return baseMapper.selectCount(new QueryWrapper<Dict>().eq("parent_id",id)) > 0;
    }

    //导出数据字典信息
    @Override
    public void exportDictData(HttpServletResponse response){
        try {
            //设置下载信息
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("dict", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            //查询数据库
            List<Dict> dictList = baseMapper.selectList(null);
            //Dict --> DictEeVo
            List<DictEeVo> dictVoList = new ArrayList<>();
            for (Dict dict:dictList){
                DictEeVo dictEeVo = new DictEeVo();
                BeanUtils.copyProperties(dict,dictEeVo);
                dictVoList.add(dictEeVo);
            }
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入
     * allEntries = true：方法调用后清空所有缓存
     * @param file
     */
    @CacheEvict(value = "dict", allEntries=true)
    @Override
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public String getNameByDictCodeAndValue(String dictCode, String value) {
        //如果dictCode为空，直接根据value查询
        if (StringUtils.isEmpty(dictCode)) {
            //根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else {
            //如果dictCode不为空，直接根据dictCode和value查询
            Dict byDictCode = this.getDictByDictCode(dictCode);
            Long parent_id = byDictCode.getId();
            //根据parent_id和value查询
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id",parent_id)
                    .eq("value",value));
            return finalDict.getName();
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict dict = this.getDictByDictCode(dictCode);
        if (dict == null) {
            return null;
        }
        return this.findChildData(dict.getId());
    }

    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }
}

