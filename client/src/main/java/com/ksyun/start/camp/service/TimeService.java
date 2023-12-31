package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.TimeDto;

import java.util.Date;

/**
 * 代表一个时间服务接口
 */
public interface TimeService {

    /**
     * 从远端服务获取当前时间
     *
     * @param style 时间格式
     * @return 指定格式的时间字符串
     */
    Date getDateTime(String style);
}