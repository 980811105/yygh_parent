package com.huawei.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Leslie
 * @create: 2021-05-24 16:16
 **/
@Configuration
@MapperScan("com.huawei.yygh.order.mapper")
public class OrderConfig {
}
