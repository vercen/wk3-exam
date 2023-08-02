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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
    public Date getDateTime(String style) {
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
                    //System.out.println("responseBody = " + responseBody);
                    //从返回的json拿到data字段数据
                    responseBody = objectMapper.readTree(responseBody).get("data").toString();
                    //System.out.println("responseBody处理后 = " + responseBody);
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
                    // 打印响应内容
                    //System.out.println("responseBody = " + responseBody);
                    //从返回的json拿到result字段数据
                    responseBody = objectMapper.readTree(responseBody).get("result").toString();
//                    responseBody = objectMapper.readTree(responseBody).get("data").toString();
                    //System.out.println("responseBody处理后 = " + responseBody);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    objectMapper.setDateFormat(dateFormat);
                    //设置时区北京
                    objectMapper.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date timeDto = objectMapper.readValue(responseBody, Date.class);
                    return timeDto;
                }
            }

        }catch (Exception e){
            log.info("获取时间失败", e);
        }

        return null;
    }
}
