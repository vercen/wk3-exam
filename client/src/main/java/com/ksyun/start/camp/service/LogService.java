package com.ksyun.start.camp.service;

import com.ksyun.start.camp.dto.LogDto;
import com.ksyun.start.camp.utils.ToJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import static com.ksyun.start.camp.ServiceAppRunner.serviceId;

/**
 * @author vercen
 * @version 1.0
 */
@Slf4j
public class LogService extends TimerTask {

    private String logUrl;
    private LogDto logDto = new LogDto();
    // 获取当前服务的名称
    private String serviceName;

    public LogService(String serviceName,String logUrl) {
        this.serviceName = serviceName;
        this.logUrl = logUrl;
    }


    @Override
    public void run() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            URI uri = new URI(logUrl + "/api/logging");
            logDto.setServiceName(serviceName);
            logDto.setServiceId(serviceId);
            //"datetime": "2023-07-25 12:34:56.235", // 日期，带有毫秒部分
            // 创建一个格式化日期的模板
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            // 获取当前日期
            Date currentDate = new Date();
            // 将日期格式化为带有毫秒部分的字符串
            String formattedDate = dateFormat.format(currentDate);
            // 将格式化后的日期字符串设置到logDto对象中
            logDto.setDatetime(formattedDate);
            logDto.setLevel("info");
            logDto.setMessage("Client status is OK.");
            String requestBody = ToJsonUtil.toJsonString(logDto);

            // 创建POST请求
            HttpPost httpPost = new HttpPost(uri.toString());
            if (requestBody != null) {
                httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
            }

            // 发送请求并获取响应
            try {
                HttpResponse response = httpClient.execute(httpPost);
                //处理响应
                if (response.getStatusLine().getStatusCode() != 200) {
                    log.error("日志发送失败"+httpPost);
                }
            }catch (Exception e) {
                log.error("日志发送失败" + httpPost);
            }
        } catch (Exception e) {
           log.error("日志发送失败",e);
        }
    }

}
