package com.ksyun.start.camp.service;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 代表简单时间服务实现
 */
@Component
public class SimpleTimeServiceImpl implements SimpleTimeService {

    @Override
    public String getDateTime(String style) {
        // 获取当前时间
        Date date = new Date();

        // 格式化时间
        String formattedDateTime;
        switch (style) {
            case "full":
                formattedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                break;
            case "date":
                formattedDateTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
                break;
            case "time":
                formattedDateTime = new SimpleDateFormat("HH:mm:ss").format(date);
                break;
            case "unix":
                formattedDateTime = String.valueOf(date.getTime());
                break;
            default:
                formattedDateTime = "Invalid style parameter. Supported styles: full, date, time, unix";
        }

        return formattedDateTime;
    }
}
