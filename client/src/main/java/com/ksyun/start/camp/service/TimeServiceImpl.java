package com.ksyun.start.camp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ksyun.start.camp.dto.RegisterDto;
import com.ksyun.start.camp.dto.TimeDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 代表远端时间服务接口实现
 */
@Component
@Slf4j
public class TimeServiceImpl implements TimeService {
    private HttpClient httpClient = HttpClients.createDefault();

    @Value("${registry.url}")
    private String registryUrl;

    private RegisterDto timeRegisterDto;

    @Override
    public TimeDto getDateTime(String style) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 连接到 registry 服务，获取远端服务列表
            URL url = new URL(registryUrl + "/api/discovery?name=time-service");
            HttpGet httpGet = new HttpGet(url.toString());
            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpGet);
            // 检查响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 获取响应内容
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 打印响应内容
                    String responseBody = EntityUtils.toString(entity);
                    // 通过Jackson库将JSON字符串反序列化为RegisterDto的列表
                    CollectionType collectionType = objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, RegisterDto.class);
                    // 将JSON字符串反序列化为RegisterDto的列表
                    List<RegisterDto> serviceInstances = objectMapper.readValue(responseBody, collectionType);
                    // 从服务实例列表中获取一个服务实例
                    timeRegisterDto = serviceInstances.get(0);
                }
            }
        } catch (IOException e) {
           return null;
        }

        // 3. 执行远程调用，获取指定格式的时间
        //http://localhost:8281/api/getDateTime
        try {
            URL url = new URL("http://"+timeRegisterDto.getIpAddress() + ":" + timeRegisterDto.getPort() + "/api/getDateTime?style=" + style);
            HttpGet httpGet = new HttpGet(url.toString());
            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpGet);
            // 检查响应状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 获取响应内容
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseBody = EntityUtils.toString(entity);
                    TimeDto timeDto = objectMapper.readValue(responseBody, TimeDto.class);
                    return timeDto;
                }
            }

        }catch (Exception e){
            log.info("获取时间失败", e);
        }

        return null;
    }
}
