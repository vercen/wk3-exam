package com.ksyun.start.camp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vercen
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String serviceName; // 服务名称
    private String serviceId;   // 服务id
    private String ipAddress;   // 服务ip地址
    private Integer port;    // 服务端口
}
