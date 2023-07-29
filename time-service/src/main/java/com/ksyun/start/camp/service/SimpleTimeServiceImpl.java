package com.ksyun.start.camp.service;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 代表简单时间服务实现
 */
@Component
public class SimpleTimeServiceImpl implements SimpleTimeService {

    @Override
    public String getDateTime(String style) {
        // 获取当前时间
        Date date = new Date();

        // 获取 GMT 时间
        TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.setTimeZone(gmtTimeZone);

        // 格式化时间
        String formattedDateTime;
        switch (style) {
            case "full":
                formatter.applyPattern("yyyy-MM-dd HH:mm:ss");
                formattedDateTime = formatter.format(date);
                break;
            case "date":
                formatter.applyPattern("yyyy-MM-dd");
                formattedDateTime = formatter.format(date);
                break;
            case "time":
                formatter.applyPattern("HH:mm:ss");
                formattedDateTime = formatter.format(date);
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
