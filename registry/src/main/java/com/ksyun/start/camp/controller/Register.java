package com.ksyun.start.camp.controller;

import com.ksyun.start.camp.dto.HeartbeatDto;
import com.ksyun.start.camp.dto.RegisterDto;
import com.ksyun.start.camp.dto.UnregisterDto;
import com.ksyun.start.camp.service.DiscoveryService;
import com.ksyun.start.camp.service.RegisterService;
import com.ksyun.start.camp.service.UnregisterService;
import com.ksyun.start.camp.vo.RespBean;
import com.ksyun.start.camp.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ksyun.start.camp.service.HeartbeatService.lastHeartbeatTimestamps;
import static com.ksyun.start.camp.service.RegisterService.serviceIdToName;
import static com.ksyun.start.camp.service.RegisterService.serviceRegistry;

/**
 * @author vercen
 * @version 1.0
 */
@RestController
@Slf4j
public class Register {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private UnregisterService unregisterService;
    @Autowired
    private DiscoveryService discoveryService;
    //服务注册
    @PostMapping("/api/register")
    public Object register(@RequestBody RegisterDto registerDto) {
        Boolean register = registerService.register(registerDto);
        if (!register) {
            return RespBean.error(RespBeanEnum.SERVICE_ALREADY_EXISTS);
        }
        //服务注册成功，更新心跳时间戳
        lastHeartbeatTimestamps.put(registerDto.getServiceId(), System.currentTimeMillis());
        return RespBean.success();
    }

    //服务注销
    @PostMapping("/api/unregister")
    public Object unregister(@RequestBody UnregisterDto unregisterDto) {
        Boolean unregister = unregisterService.unregister(unregisterDto);
        if (!unregister) {
            return RespBean.error(RespBeanEnum.SERVICE_NOT_EXISTS);
        }
        return RespBean.success(RespBeanEnum.UNREGISTERSUCCESS);
    }

    //服务心跳
    @PostMapping("/api/heartbeat")
    public Object heartbeat(@RequestBody HeartbeatDto heartbeatDto) {
        String serviceId = heartbeatDto.getServiceId();
        if (serviceId != null&&serviceIdToName.containsKey(serviceId)) {
            //增加防御检测
            String serviceName = serviceIdToName.get(serviceId);
            RegisterDto registerDto = serviceRegistry.get(serviceName).get(serviceId);

            if (registerDto.getIpAddress().equals(heartbeatDto.getIpAddress())&&
                    registerDto.getPort().equals(heartbeatDto.getPort())) {
                log.info("检测到服务实例 {} 心跳", serviceId);
                lastHeartbeatTimestamps.put(serviceId, System.currentTimeMillis());
                return RespBean.success(RespBeanEnum.HEARTBEAT);
            }else {
                return RespBean.error(RespBeanEnum.SERVICE_NOT_EXISTS);
            }
        }else {
            return RespBean.error(RespBeanEnum.SERVICE_NOT_EXISTS);
        }
    }

    //服务发现
    @GetMapping ("/api/discovery")
    @ResponseBody
    public Object discovery(@RequestParam(required = false) String name) {
        if (name != null) {
            log.info("服务发现 {}" , name);
            return RespBean.success(RespBeanEnum.SERVICEFIND,discoveryService.get(name));
        } else {
            return RespBean.success(RespBeanEnum.SERVICEFIND,discoveryService.getAll()) ;
        }
    }

}
