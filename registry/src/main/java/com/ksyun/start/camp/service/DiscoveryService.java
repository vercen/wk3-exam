package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.RegisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ksyun.start.camp.service.RegisterService.serviceRegistry;

/**
 * @author vercen
 * @version 1.0
 */
@Service
@Slf4j
public class DiscoveryService {
    private static Map<String, Integer> currentIndexMap = new HashMap<>();
    public List<RegisterDto> get(String name) {
        List<RegisterDto> registerDtos = new ArrayList<>();
        if (serviceRegistry.containsKey(name)) {
            // 服务名称存在，获取服务信息
            log.info("服务 {} 存在", name);
            // 获取服务实例列表
            Map<String, RegisterDto> serviceInstances = serviceRegistry.get(name);

            // 获取服务实例数量
            if (!serviceInstances.isEmpty()) {
                // 获取当前获取的服务实例索引
                int currentIndex = currentIndexMap.getOrDefault(name, 0);
                log.info("当前获取的服务实例索引为：{}", currentIndex);
                // 获取服务实例数量
                int numInstances = serviceInstances.size();
                log.info("服务 {} 有 {} 个实例，当前获取第 {} 个实例", name, numInstances, currentIndex);
                log.info("输出所有服务实例信息：{}", serviceInstances.values().toString());
                List<RegisterDto> registerDtoList = new ArrayList<>(serviceInstances.values());
                //轮询取值
                RegisterDto registerDto = registerDtoList.get(currentIndex % numInstances);
                registerDtos.add(registerDto);
                // 将服务实例添加到返回列表
                currentIndex = (currentIndex + 1) % numInstances;
                // 重置索引
                currentIndexMap.put(name, currentIndex);

//                int i = 0;
//                // 遍历服务实例列表，获取当前索引的服务实例
//                for (RegisterDto instance : serviceInstances.values()) {
//                    // 如果当前索引等于服务实例数量，重置索引
//                    if (i == currentIndex) {
//                        // 将服务实例添加到返回列表
//                        registerDtos.add(instance);
//                        // 重置索引
//                        currentIndex = (currentIndex + 1) % numInstances;
//                        break;
//                    }
//                    i++;
//                }
//                currentIndexMap.put(name, currentIndex);
            }
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
