package com.ksyun.start.camp.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author vercen
 * @version 1.0
 * 服务注册对象dto
 * 同一个服务的不同实例，服务名应该相同
 * 服务唯一标识，即服务ID，服务名+服务ID唯一确定一个服务
 */
@Data
public class RegisterDto implements Serializable {
    private String serviceName; // 服务名称
    private String serviceId;   // 服务id
    private String ipAddress;   // 服务ip地址
    private Integer port;    // 服务端口

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterDto that = (RegisterDto) o;
        return Objects.equals(serviceName, that.serviceName) && Objects.equals(serviceId, that.serviceId) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, serviceId, ipAddress, port);
    }
}
