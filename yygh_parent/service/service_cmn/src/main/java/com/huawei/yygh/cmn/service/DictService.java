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
    void exportDictData(HttpServletResponse response);
    void importDictData(MultipartFile file);
}
