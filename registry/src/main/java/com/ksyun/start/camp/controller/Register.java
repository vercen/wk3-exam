package com.ksyun.start.camp.controller;

import com.ksyun.start.camp.dto.RegisterDto;
import com.ksyun.start.camp.service.RegisterService;
import com.ksyun.start.camp.vo.RespBean;
import com.ksyun.start.camp.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vercen
 * @version 1.0
 */
@RestController
public class Register {
    @Autowired
    private RegisterService registerService;
    //服务注册
    @PostMapping("/api/register")
    public Object register(@RequestBody RegisterDto registerDto) {
        Boolean register = registerService.register(registerDto);
        if (!register) {
            return RespBean.error(RespBeanEnum.SERVICE_ALREADY_EXISTS);
        }
        return RespBean.success();
    }
}
