package com.huawei.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Leslie
 * @create: 2021-05-21 12:36
 **/
@Configuration
@MapperScan("com.huawei.yygh.user.mapper")
public class UserConfig {
}
