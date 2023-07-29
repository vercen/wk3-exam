package com.ksyun.start.camp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksyun.start.camp.dto.HeartbeatDto;
import com.ksyun.start.camp.dto.RegisterDto;
import com.ksyun.start.camp.utils.ToJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * 服务启动运行逻辑
 */
@Component
@Slf4j
public class ServiceAppRunner implements ApplicationRunner {
    @Autowired
    private ObjectMapper objectMapper;

    // 从配置文件中获取注册中心的地址
    @Value("${registry.url}")
    private String registryUrl;

    // 获取当前服务的名称
    @Value("${spring.application.name}")
    private String serviceName;

    // 从配置文件中获取当前服务的主机地址
    @Value("${service.host}")
    private String serviceHost;

    // 从配置文件中获取当前服务的端口号
    @Value("${server.port}")
    private int servicePort;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String serviceId = UUID.randomUUID().toString();
        RegisterDto registerDto = new RegisterDto(serviceName, serviceId, serviceHost, servicePort);
        // 向 registry 服务注册当前服务
        registerService(registerDto);

        // 定期发送心跳逻辑
        Timer timer = new Timer();
        timer.schedule(new HeartbeatTask(serviceId), 0, 30000); // 每30秒发送一次心跳
    }

    // 向注册中心注册当前服务
    private void registerService(RegisterDto registerDto) {
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 构建注册服务的URL
            URL url = new URL(registryUrl + "/api/register");

            //将RegisterDto对象转换成JSON字符串
            String requestBody = ToJsonUtil.toJsonString(registerDto);

            // 创建POST请求
            HttpPost httpPost = new HttpPost(url.toString());
            httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);

            // 处理响应
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("注册成功");
            } else {
                log.info("注册失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 心跳任务，定期发送心跳请求
    private class HeartbeatTask extends TimerTask {
        private final String serviceId;
        public HeartbeatTask(String serviceId) {
            this.serviceId = serviceId;
        }
        @Override
        public void run() {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                // 构建心跳请求的URL
                URL url = new URL(registryUrl + "/api/heartbeat");

                HeartbeatDto heartbeatDto = new HeartbeatDto(serviceId, serviceHost, servicePort);
                // 将 HeartbeatDto 对象转换成 JSON 字符串
                String requestBody = ToJsonUtil.toJsonString(heartbeatDto);

                // 创建 POST 请求
                HttpPost httpPost = new HttpPost(url.toString());
                httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));

                // 发送请求并获取响应
                HttpResponse response = httpClient.execute(httpPost);

                // 处理响应
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    log.info("心跳发送成功");
                } else {
                    log.info("心跳发送失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
