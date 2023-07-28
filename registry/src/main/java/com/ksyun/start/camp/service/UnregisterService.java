package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.RegisterDto;
import com.ksyun.start.camp.dto.UnregisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.ksyun.start.camp.service.HeartbeatService.lastHeartbeatTimestamps;
import static com.ksyun.start.camp.service.RegisterService.serviceRegistry;

/**
 * @author vercen
 * @version 1.0
 */
@Service
@Slf4j
public class UnregisterService {
    //服务注销
    public Boolean unregister(UnregisterDto unregisterDto) {
        //判断服务名称是否存在
        if (serviceRegistry.containsKey(unregisterDto.getServiceName())) {
            //服务名称存在，判断服务id是否存在
            Map<String, RegisterDto> serviceMap = serviceRegistry.get(unregisterDto.getServiceName());
            if (serviceMap.containsKey(unregisterDto.getServiceId())) {
                //服务id存在，删除服务信息
                serviceMap.remove(unregisterDto.getServiceId());
                log.info("服务实例 {} 注销成功", unregisterDto.getServiceId());
            } else {
                //服务id不存在，注销失败
                return false;
            }
        } else {
            //服务名称不存在，注销失败
            return false;
        }
        // 从心跳表中移除心跳超时的服务实例
        lastHeartbeatTimestamps.remove(unregisterDto.getServiceId());
        return true;
    }
}
