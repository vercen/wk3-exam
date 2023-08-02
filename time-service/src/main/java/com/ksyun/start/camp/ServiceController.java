package com.ksyun.start.camp;

import com.ksyun.start.camp.service.SimpleTimeServiceImpl;
import com.ksyun.start.camp.utils.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ksyun.start.camp.ServiceAppRunner.serviceId;

@RestController
@RequestMapping("/api")
public class ServiceController {
    @Autowired
    private SimpleTimeServiceImpl simpleTimeService;

    // 在此实现简单时间服务的接口逻辑
    // 1. 调用 SimpleTimeService
    @RequestMapping ("/getDateTime")
    public Object getDateTime(@RequestParam("style") String style) {
        String dateTime = simpleTimeService.getDateTime(style);
        if (dateTime == null) {
            return ApiResponse.error("参数样式不正确。", serviceId);
//            return RespBean.error("参数样式不正确。");
        }
        return ApiResponse.success(dateTime, serviceId);
//        ApiResponse apiResponse = new ApiResponse(dateTime, serviceId);
//        return RespBean.success(apiResponse);
    }

}
