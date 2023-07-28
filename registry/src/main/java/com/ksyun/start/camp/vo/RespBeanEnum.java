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
    SUCCESS(200, "服务注册成功"),
    // 服务注销成功
    UNREGISTERSUCCESS(200, "服务注销成功"),
    //心跳成功
    HEARTBEAT(200, "心跳成功"),
    // 服务注册失败
    ERROR(500, "服务注册失败"),
    // 服务已经存在
    SERVICE_ALREADY_EXISTS(501, "服务已经存在"),
    // 服务不存在
    SERVICE_NOT_EXISTS(502, "服务不存在"),
    // 服务注册参数错误
    SERVICE_REGISTER_PARAM_ERROR(503, "服务注册参数错误");
    // 服务注销成功
    // 服务注销失败
    // 服务注销参数错误

    private final Integer code;
    private final String message;

}
