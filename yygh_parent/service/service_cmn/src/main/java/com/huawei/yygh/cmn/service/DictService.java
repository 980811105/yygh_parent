package com.huawei.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huawei.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: Leslie
 * @create: 2021-04-25 11:40
 **/
public interface DictService extends IService<Dict> {
    //根据数据id查询子数据列表
    List<Dict> findChildData(Long id);
    //导出数据字典接口
    void exportDictData(HttpServletResponse response);
    //导入数据字典
    void importDictData(MultipartFile file);
    //根据dictcode和value查询
    String getNameByDictCodeAndValue(String dictCode, String value);
    //根据dictCode获取下级节点
    List<Dict> findByDictCode(String dictCode);
}
