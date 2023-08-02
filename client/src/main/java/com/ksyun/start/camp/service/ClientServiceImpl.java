package com.ksyun.start.camp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ksyun.start.camp.ServiceAppRunner.serviceId;

/**
 * 客户端服务实现
 */
@Component
public class ClientServiceImpl implements ClientService {
    @Autowired
    private TimeService timeService;


    @Override
    public String getInfo() {
        Date date = timeService.getDateTime("full");
        //设置为中国时区
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        String beijingTime = sdf.format(date);

        // 开始编写你的逻辑，下面是提示
        // 1. 调用 TimeService 获取远端服务返回的时间
        // 2. 获取到自身的 serviceId 信息
        // 3. 组合相关信息返回
        //"Hello Kingsoft Clound Star Camp - [服务 ID] - 2023-07-25 12:34:56"
        // 设置日期格式
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        // 格式化为北京时间字符串
//        String beijingTime = formatter.format(result);
//        String serviceId = timeDto.getServiceId();
        //"Hello Kingsoft Clound Star Camp - [服务 ID] - 2023-07-25 12:34:56"
        return "Hello Kingsoft Clound Star Camp - " + serviceId + " - " + beijingTime;
    }
}
