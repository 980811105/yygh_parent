package com.huawei.yygh.cmn.controller;

import com.huawei.yygh.cmn.service.DictService;
import com.huawei.yygh.common.result.Result;
import com.huawei.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: Leslie
 * @create: 2021-04-29 21:34
 **/
@Api(tags = "数据字典接口" )
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Resource
    private DictService dictService;

    //导出数据字典接口
    @ApiOperation(value = "导出")
    @GetMapping("exportData")
    public void exportDict(HttpServletResponse response){
        dictService.exportDictData(response);
    }

    //导入数据字典
    @PostMapping("importData")
    public Result importDict(MultipartFile file){
        dictService.importDictData(file);
        return Result.ok();
    }

    //根据数据id查询子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> childData = dictService.findChildData(id);
        return Result.ok(childData);
    }

    //根据dictcode和value查询
    @ApiOperation(value = "获取数据字典名称")
    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value){
        return dictService.getNameByDictCodeAndValue(dictCode,value);
    }
    /**
     * Description:根据value查询
     * @param: * @param value
     * @return:java.lang.String
     */
    @ApiOperation(value = "获取数据字典名称")
    @GetMapping("getName/{value}")
    public String getName(@PathVariable("value") String value){
        return dictService.getNameByDictCodeAndValue("",value);
    }
    /**
     * Description: 根据dictCode获取下级节点
     * @param: * @param dictCode
     * @return:com.huawei.yygh.common.result.Result<java.util.List<com.huawei.yygh.model.cmn.Dict>>
     */
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

}
