package com.ksyun.start.camp.utils;

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
public class RespBean {
    /**
     * 代表此 API 的响应返回码
     * 200 表示成功，非 200 表示失败
     */
    private int code;
    private Object data;

    public static RespBean success(Object obj) {
        return new RespBean(200, obj);
    }
    public static RespBean error(Object obj) {
        return new RespBean(500, obj);
    }


}
