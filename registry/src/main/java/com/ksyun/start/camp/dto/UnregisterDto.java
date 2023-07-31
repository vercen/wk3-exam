package com.ksyun.start.camp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * @author vercen
 * @version 1.0
 * 服务注销对象dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UnregisterDto {
    private String serviceName; // 服务名称
    private String serviceId;   // 服务id
    private String ipAddress;   // 服务ip地址
    private Integer port;    // 服务端口

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnregisterDto that = (UnregisterDto) o;
        return Objects.equals(serviceName, that.serviceName) && Objects.equals(serviceId, that.serviceId) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, serviceId, ipAddress, port);
    }
}
