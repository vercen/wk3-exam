package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.RegisterDto;
import com.ksyun.start.camp.dto.UnregisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static com.ksyun.start.camp.service.HeartbeatService.lastHeartbeatTimestamps;
import static com.ksyun.start.camp.service.RegisterService.serviceIdToName;
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
                //判断信息是否一样
                RegisterDto registerDto = serviceMap.get(unregisterDto.getServiceId());
                System.out.println( "registerDto = " + registerDto.toString());
                System.out.println( "unregisterDto = " + unregisterDto.toString());
                if (Objects.equals(registerDto.getIpAddress(), unregisterDto.getIpAddress()) && Objects.equals(registerDto.getPort(), unregisterDto.getPort())) {
                    serviceMap.remove(unregisterDto.getServiceId());
                    log.info("服务实例 {} 注销成功", unregisterDto.getServiceId());
                } else {
                    log.info("服务实例 {} 注销失败,存在攻击行为，非服务端发起 ", unregisterDto.getServiceId());
                    return false;
                }
            } else {
                //服务id不存在，注销失败
                return false;
            }
        } else {
            //服务名称不存在，注销失败
            return false;
        }

        // 从服务id对应服务名称表中移除服务id
        serviceIdToName.remove(unregisterDto.getServiceId());
        // 从心跳表中移除心跳超时的服务实例
        lastHeartbeatTimestamps.remove(unregisterDto.getServiceId());
        return true;
    }
}
