package com.ksyun.start.camp;

import com.ksyun.start.camp.dto.LogMessageDto;
import com.ksyun.start.camp.service.LoggingService;
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
    public String logMessage(@RequestBody LogMessageDto logMessage) {
        boolean success = loggingService.saveLog(logMessage);
        if (success) {
            return "日志保存成功。";
        } else {
            return "保存日志失败。";
        }
    }

    @GetMapping("/list")
    public List<LogMessageDto> getLogs(@RequestParam(required = false) String service) {
        if (service != null) {
            return loggingService.getLogsByService(service);
        } else {
            return loggingService.getAllLogs();
        }
    }


}
