package com.huawei.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huawei.yygh.model.order.OrderInfo;
import com.huawei.yygh.vo.order.OrderCountQueryVo;
import com.huawei.yygh.vo.order.OrderCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Leslie
 * @create: 2021-05-24 16:03
 **/
public interface OrderMapper extends BaseMapper<OrderInfo>{
    //查询预约统计数据的方法
    List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo orderCountQueryVo);
}
