package com.ksyun.start.camp;

import com.ksyun.start.camp.dto.LogMessageDto;
import com.ksyun.start.camp.service.LoggingService;
import com.ksyun.start.camp.utils.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实现日志服务 API
 */
@RestController
@RequestMapping("/api")
public class ServiceController {
    @Autowired
    private LoggingService loggingService;

    // TODO: 实现日志服务 API
    @PostMapping("/logging")
    public Object logMessage(@RequestBody LogMessageDto logMessage) {
        boolean success = loggingService.saveLog(logMessage);
        if (success) {
            return RespBean.success("保存日志成功。");
        } else {
            return RespBean.error("保存日志失败。");
        }
    }

    @GetMapping("/list")
    public Object getLogs(@RequestParam(required = false) String service) {
        if (service != null) {
            return RespBean.success(loggingService.getLogsByService(service)) ;
        } else {
            return RespBean.success(loggingService.getAllLogs()) ;
        }
    }


}
