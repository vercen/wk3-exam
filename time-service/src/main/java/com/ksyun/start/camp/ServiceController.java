package com.ksyun.start.camp;

import com.ksyun.start.camp.service.SimpleTimeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ServiceController {
    @Autowired
    private SimpleTimeServiceImpl simpleTimeService;

    // 在此实现简单时间服务的接口逻辑
    // 1. 调用 SimpleTimeService
    @GetMapping("/getDateTime")
    public ApiResponse getDateTime(@RequestParam("style") String style) {
        String dateTime = simpleTimeService.getDateTime(style);
        return new ApiResponse(200, dateTime);
    }

}
