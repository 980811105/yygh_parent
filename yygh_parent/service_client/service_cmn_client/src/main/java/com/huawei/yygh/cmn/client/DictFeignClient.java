package com.huawei.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: Leslie
 * @create: 2021-05-13 17:41
 **/
@FeignClient("service-cmn")
@Service
public interface DictFeignClient {

    @GetMapping("/admin/cmn/dict/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,@PathVariable("value") String value);

    @GetMapping("/admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable("value") String value);
}
