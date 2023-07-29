package com.ksyun.start.camp.dto;

/**
 * @author vercen
 * @version 1.0
 */
public class LogMessageDao {
    //{
    //    "logId": 5, // 需保证有序无重复
    //    "serviceName": "service1",
    //    "serviceId": "service-1",
    //    "datetime": "2023-07-25 12:34:56.235",
    //    "level": "info",
    //    "message": "..." // 省略
    //    },
    private int logId;
    private String serviceName;
    private String serviceId;
    private String datetime;
    private String level;
    private String message;
}
