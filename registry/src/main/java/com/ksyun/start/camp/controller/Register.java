package com.ksyun.start.camp.controller;

import com.ksyun.start.camp.dto.RegisterDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vercen
 * @version 1.0
 */
@RestController
public class Register {
    //POST /api/register
    //服务注册
    @PostMapping("/api/register")
    public void register(RegisterDto registerDto) {
        //TODO

    }
}
