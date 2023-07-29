package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.LogMessageDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.hash;

/**
 * 日志服务的实现
 */
@Component
public class LoggingServiceImpl implements LoggingService {
    private final Map<String, TreeSet<LogMessageDto>> logsByService = new ConcurrentHashMap<>();
    private final HashSet<Integer> allLogs = new HashSet<>();
    private int logId = 1;

    public boolean saveLog(LogMessageDto logMessage) {
        if (logMessage == null) {
            return false;
        }

        String serviceId = logMessage.getServiceId();
        //查询 serviceId 对应的日志集合，如果不存在则创建
        TreeSet<LogMessageDto> logs = logsByService.computeIfAbsent(serviceId, key -> new TreeSet<>(Comparator.comparingInt(LogMessageDto::getLogId)));

        // TODO: 如果需要，你可以在这里实现去重逻辑。
        // 例如，在添加日志之前检查日志是否已存在于日志集合中。

        synchronized (this) {
            //判断是否已存在
            if (allLogs.contains(hash(logMessage))) {
                return false;
            }
            allLogs.add(hash(logMessage));
            logMessage.setLogId(logId++);
            logs.add(logMessage);

        }
        return true;
    }

    public List<LogMessageDto> getLogsByService(String serviceId) {
        TreeSet<LogMessageDto> logs = logsByService.getOrDefault(serviceId, new TreeSet<>(Comparator.comparingInt(LogMessageDto::getLogId)));
        List<LogMessageDto> resultList = new ArrayList<>(logs.descendingSet());
        return resultList.subList(0, Math.min(5, resultList.size()));
    }

    public List<LogMessageDto> getAllLogs() {
        List<LogMessageDto> allLogs = new ArrayList<>();
        for (TreeSet<LogMessageDto> logs : logsByService.values()) {
            allLogs.addAll(logs);
        }
        allLogs.sort(Comparator.comparingInt(LogMessageDto::getLogId).reversed());
        //如果不带可选参数 service，则显示全部记录，按记录 logId 倒序排列

        return allLogs;
    }
}