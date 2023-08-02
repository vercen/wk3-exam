package com.ksyun.start.camp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代表此 API 的返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    /**
     * 代表此 API 的响应返回码
     * 200 表示成功，非 200 表示失败
     */
    private int code = 200;

    private String result;

    private String serviceId;

    public ApiResponse(String result, String serviceId) {
        this.result = result;
        this.serviceId = serviceId;
    }
    public static ApiResponse success(String result, String serviceId) {
        return new ApiResponse(200, result, serviceId);
    }

    public static ApiResponse error(String result, String serviceId) {
        return new ApiResponse(500, result, serviceId);
    }
}
