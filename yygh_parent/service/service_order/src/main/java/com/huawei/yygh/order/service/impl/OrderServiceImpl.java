package com.huawei.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huawei.common.rabbit.constant.MqConst;
import com.huawei.common.rabbit.service.RabbitService;
import com.huawei.yygh.common.exception.YyghException;
import com.huawei.yygh.common.helper.HttpRequestHelper;
import com.huawei.yygh.common.result.ResultCodeEnum;
import com.huawei.yygh.enums.OrderStatusEnum;
import com.huawei.yygh.hosp.client.HospitalFeignClient;
import com.huawei.yygh.model.order.OrderInfo;
import com.huawei.yygh.model.user.Patient;
import com.huawei.yygh.order.mapper.OrderMapper;
import com.huawei.yygh.order.service.OrderService;
import com.huawei.yygh.user.client.PatientFeignClient;
import com.huawei.yygh.vo.hosp.ScheduleOrderVo;
import com.huawei.yygh.vo.msm.MsmVo;
import com.huawei.yygh.vo.order.OrderMqVo;
import com.huawei.yygh.vo.order.SignInfoVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: Leslie
 * @create: 2021-05-24 16:07
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,OrderInfo> implements OrderService {
    @Resource
    private PatientFeignClient patientFeignClient;
    @Resource
    private HospitalFeignClient hospitalFeignClient;
    @Resource
    private RabbitService rabbitService;

    //生成挂号订单
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        //获取就诊人信息
        //TODO patient做非空判断
        Patient patient = patientFeignClient.getPatient(patientId);
        //获取排班相关信息
        //TODO scheduleOrderVo做非空判断
        ScheduleOrderVo scheduleOrderVo = hospitalFeignClient.getScheduleOrderVo(scheduleId);
        //判断当前时间是否还可以预约
        /*if(new DateTime(scheduleOrderVo.getStartTime()).isAfterNow()
                || new DateTime(scheduleOrderVo.getEndTime()).isBeforeNow()) {
            throw new YyghException(ResultCodeEnum.TIME_NO);
        }*/
        //获取签名信息
        //TODO signInfoVo做非空判断
        SignInfoVo signInfoVo = hospitalFeignClient.getSignInfoVo(scheduleOrderVo.getHoscode());
        //添加到订单表
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(scheduleOrderVo,orderInfo);
        //向orderInfo中设置其他数据
        String outTradeNo = System.currentTimeMillis() + ""+ new Random().nextInt(100);
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setScheduleId(scheduleId);
        orderInfo.setUserId(patient.getUserId());
        orderInfo.setPatientId(patientId);
        orderInfo.setPatientName(patient.getName());
        orderInfo.setPatientPhone(patient.getPhone());
        orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());
        baseMapper.insert(orderInfo);

        //调用医院接口，实现预约挂号操作
        //设置调用医院接口需要的参数，将参数放到map集合
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode",orderInfo.getHoscode());
        paramMap.put("depcode",orderInfo.getDepcode());
        paramMap.put("hosScheduleId",orderInfo.getScheduleId());
        paramMap.put("reserveDate",new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd"));
        paramMap.put("reserveTime", orderInfo.getReserveTime());
        paramMap.put("amount",orderInfo.getAmount());
        paramMap.put("name", patient.getName());
        paramMap.put("certificatesType",patient.getCertificatesType());
        paramMap.put("certificatesNo", patient.getCertificatesNo());
        paramMap.put("sex",patient.getSex());
        paramMap.put("birthdate", patient.getBirthdate());
        paramMap.put("phone",patient.getPhone());
        paramMap.put("isMarry", patient.getIsMarry());
        paramMap.put("provinceCode",patient.getProvinceCode());
        paramMap.put("cityCode", patient.getCityCode());
        paramMap.put("districtCode",patient.getDistrictCode());
        paramMap.put("address",patient.getAddress());
        //联系人
        paramMap.put("contactsName",patient.getContactsName());
        paramMap.put("contactsCertificatesType", patient.getContactsCertificatesType());
        paramMap.put("contactsCertificatesNo",patient.getContactsCertificatesNo());
        paramMap.put("contactsPhone",patient.getContactsPhone());
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(paramMap, signInfoVo.getSignKey());
        paramMap.put("sign", sign);

        //请求医院系统接口
        JSONObject result = HttpRequestHelper.sendRequest(paramMap, signInfoVo.getApiUrl() + "/order/submitOrder");

        if(result.getInteger("code") == 200) {
            JSONObject jsonObject = result.getJSONObject("data");
            //预约记录唯一标识（医院预约记录主键）
            String hosRecordId = jsonObject.getString("hosRecordId");
            //预约序号
            Integer number = jsonObject.getInteger("number");
            //取号时间
            String fetchTime = jsonObject.getString("fetchTime");
            //取号地址
            String fetchAddress = jsonObject.getString("fetchAddress");
            //更新订单
            orderInfo.setHosRecordId(hosRecordId);
            orderInfo.setNumber(number);
            orderInfo.setFetchTime(fetchTime);
            orderInfo.setFetchAddress(fetchAddress);
            baseMapper.updateById(orderInfo);

            //排班可预约数
            Integer reservedNumber = jsonObject.getInteger("reservedNumber");
            //排班剩余预约数
            Integer availableNumber = jsonObject.getInteger("availableNumber");
            //发送mq消息，号源更新和短信通知
            OrderMqVo orderMqVo = new OrderMqVo();
            orderMqVo.setScheduleId(scheduleId);
            orderMqVo.setReservedNumber(reservedNumber);
            orderMqVo.setAvailableNumber(availableNumber);

            //短信提示
            MsmVo msmVo = new MsmVo();
            msmVo.setPhone(orderInfo.getPatientPhone());
            msmVo.setTemplateCode("SMS_15971941807");
            String reserveDate =
                    new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd")
                            + (orderInfo.getReserveTime()==0 ? "上午": "下午");
            Map<String,Object> param = new HashMap<String,Object>(){{
                put("title", orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle());
                put("amount", orderInfo.getAmount());
                put("reserveDate", reserveDate);
                put("name", orderInfo.getPatientName());
                put("quitTime", new DateTime(orderInfo.getQuitTime()).toString("yyyy-MM-dd HH:mm"));
            }};
            msmVo.setParam(param);

            orderMqVo.setMsmVo(msmVo);
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);

        }else {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }
            return orderInfo.getId();
    }

    //根据订单id查询订单详情
    @Override
    public OrderInfo getOrders(String orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        return this.packOrderInfo(orderInfo);
    }

    private OrderInfo packOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put("orderStatusString", OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
        return orderInfo;
    }

}
