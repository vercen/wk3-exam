package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.RegisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vercen
 * @version 1.0
 */
@Service
@Slf4j
public class RegisterService {
    //服务列表 key:服务名称 value:服务信息
    //{time-service,{serviceId,服务信息}}
    public static Map<String, Map<String,RegisterDto>> serviceRegistry = new ConcurrentHashMap<>();
    //服务id对应服务名称
    public static Map<String, String> serviceIdToName = new ConcurrentHashMap<>();
    //服务注册
    public Boolean register(RegisterDto registerDto) {
        //第一次服务注册，初始化服务列表
        //先判断服务名称是否存在
        if (serviceRegistry.containsKey(registerDto.getServiceName())) {
            //服务名称存在，判断服务id是否存在
            Map<String, RegisterDto> serviceMap = serviceRegistry.get(registerDto.getServiceName());
            if (serviceMap.containsKey(registerDto.getServiceId())) {
                //服务id存在，注册失败
                return false;
            } else {
                //服务id不存在，新增服务信息
                serviceMap.put(registerDto.getServiceId(), registerDto);
                log.info("服务 {} 实例 {} 注册成功", registerDto.getServiceName(), registerDto.getServiceId());
            }
        } else {
            if (serviceIdToName.containsKey(registerDto.getServiceId())) {
                //服务id存在，注册失败
                return false;
            }
            //服务名称不存在，新增服务名称
            Map<String, RegisterDto> serviceMap = new ConcurrentHashMap<>();
            serviceMap.put(registerDto.getServiceId(), registerDto);
            serviceRegistry.put(registerDto.getServiceName(), serviceMap);
            log.info("服务 {} 实例 {} 注册成功", registerDto.getServiceName(), registerDto.getServiceId());
        }
        serviceIdToName.put(registerDto.getServiceId(), registerDto.getServiceName());
        return true;
    }
}
