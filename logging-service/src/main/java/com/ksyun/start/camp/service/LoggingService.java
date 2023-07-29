package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.LogMessageDto;

import java.util.List;

/**
 * 日志服务实现接口
 */
public interface LoggingService {

    // TODO: 实现日志服务接口
    // 此处不再重复提示骨架代码，可参考其他 Service 接口的定义
    /**
     * 保存日志记录
     *
     * @param logMessage 日志记录对象
     * @return true表示保存成功，false表示保存失败
     */
    boolean saveLog(LogMessageDto logMessage);

    /**
     * 根据服务ID获取日志信息列表
     *
     * @param serviceId 服务ID
     * @return 包含日志信息的列表
     */
    List<LogMessageDto> getLogsByService(String serviceId);

    /**
     * 获取所有日志信息列表
     *
     * @return 包含所有日志信息的列表
     */
    List<LogMessageDto> getAllLogs();


}
