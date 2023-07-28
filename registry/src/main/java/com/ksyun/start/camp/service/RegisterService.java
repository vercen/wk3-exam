package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.RegisterDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vercen
 * @version 1.0
 */
@Service
public class RegisterService {
    public static Map<String, Map<String,RegisterDto>> serviceRegistry = new ConcurrentHashMap<>();
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
            }
        } else {
            //服务名称不存在，新增服务名称
            Map<String, RegisterDto> serviceMap = new ConcurrentHashMap<>();
            serviceMap.put(registerDto.getServiceId(), registerDto);
            serviceRegistry.put(registerDto.getServiceName(), serviceMap);
        }
        return true;
    }
}
