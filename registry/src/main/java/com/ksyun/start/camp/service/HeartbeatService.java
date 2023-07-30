package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.UnregisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.ksyun.start.camp.service.RegisterService.serviceIdToName;

/**
 * @author vercen
 * @version 1.0
 */
@Service
@Slf4j
public class HeartbeatService {
    @Autowired
    private UnregisterService unregisterService;
    public static final Map<String, Long> lastHeartbeatTimestamps = new ConcurrentHashMap<>();
    private static final long HEARTBEAT_TIMEOUT = 60000; // 心跳超时时间为60秒
    // 定时任务，每隔一段时间检查心跳超时
    @Scheduled(fixedDelay = 2000)   // 每隔2秒执行一次
    public void checkHeartbeatTimeout() {
        long currentTimestamp = System.currentTimeMillis();
        Set<String> inactiveServiceIds = new HashSet<>();
        // 遍历所有服务实例的最后心跳时间
        for (Map.Entry<String, Long> entry : lastHeartbeatTimestamps.entrySet()) {
            String serviceId = entry.getKey();
            long lastHeartbeatTime = entry.getValue();
            if (currentTimestamp - lastHeartbeatTime > HEARTBEAT_TIMEOUT) {
                inactiveServiceIds.add(serviceId);
            }
        }
        // 从注册表中移除心跳超时的服务实例
        for (String serviceId : inactiveServiceIds) {
            log.info("服务实例 {} 心跳超时", serviceId);
            String serviceName = serviceIdToName.get(serviceId);
            UnregisterDto unregisterDto = new UnregisterDto();
            unregisterDto.setServiceId(serviceId);
            unregisterDto.setServiceName(serviceName);
            unregisterService.unregister(unregisterDto);
        }
        // 从心跳表中移除心跳超时的服务实例
        lastHeartbeatTimestamps.keySet().removeAll(inactiveServiceIds);
    }

}
