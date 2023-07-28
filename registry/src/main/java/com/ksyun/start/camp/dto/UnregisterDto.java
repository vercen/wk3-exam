package com.ksyun.start.camp.dto;

import lombok.Data;

/**
 * @author vercen
 * @version 1.0
 * 服务注销对象dto
 */
@Data
public class UnregisterDto {
    private String serviceName; // 服务名称
    private String serviceId;   // 服务id
    private String ipAddress;   // 服务ip地址
    private Integer port;    // 服务端口
}
