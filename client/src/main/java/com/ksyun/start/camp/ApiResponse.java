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

    private String error;

    private String result;


    public static ApiResponse success(String result) {
        return new ApiResponse(200,null, result);
    }

    public static ApiResponse error( String error) {
        return new ApiResponse(500,error, null);
    }
}
