package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.RegisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ksyun.start.camp.service.RegisterService.serviceRegistry;

/**
 * @author vercen
 * @version 1.0
 */
@Service
@Slf4j
public class DiscoveryService {
    //get(name)
    public List<RegisterDto> get(String name) {
        List<RegisterDto> registerDtos = new ArrayList<>();
        if (serviceRegistry.containsKey(name)) {
            //服务名称存在，获取服务信息
            log.info("服务 {} 存在", name);
            registerDtos.addAll(serviceRegistry.get(name).values());
        }
        return registerDtos;
    }
    //getAll()
    public List<RegisterDto> getAll() {
        List<RegisterDto> registerDtos = new ArrayList<>();
        for (String serviceName : serviceRegistry.keySet()) {
            registerDtos.addAll(serviceRegistry.get(serviceName).values());
        }
        return registerDtos;
    }
}
