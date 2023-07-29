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
public class HeartbeatDto {
    private String serviceId;   // 服务唯一标识，即服务ID
    private String ipAddress;   // 服务ip地址
    private Integer port;   // 服务端口
}
