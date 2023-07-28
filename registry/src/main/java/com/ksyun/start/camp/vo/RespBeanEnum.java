package com.ksyun.start.camp.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author vercen
 * @version 1.0
 */
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    // 服务注册成功
    SUCCESS(200, "SUCCESS"),
    // 服务注册失败
    ERROR(500, "服务注册失败"),
    // 服务已经存在
    SERVICE_ALREADY_EXISTS(500, "服务已经存在"),
    // 服务不存在
    SERVICE_NOT_EXISTS(500, "服务不存在"),
    // 服务注册参数错误
    SERVICE_REGISTER_PARAM_ERROR(500, "服务注册参数错误");

    private final Integer code;
    private final String message;

}
