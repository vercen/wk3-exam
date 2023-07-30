package com.ksyun.start.camp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

import java.util.Date;

/**
 * @author vercen
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDto {
//    {
//    "serviceName": "client", // 服务名
//    "serviceId": "client-1", // 服务ID，设置为你实现的客户端的 ID 实际值
//    "datetime": "2023-07-25 12:34:56.235", // 日期，带有毫秒部分
//    "level": "info", // 级别，目前只有 info
//    "message": "Client status is OK." // 消息内容，可任意
//}
    private String serviceName;
    private String serviceId;
    private String datetime;
    private String level;
    private String message;

}
