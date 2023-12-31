package com.ksyun.start.camp;

import com.ksyun.start.camp.dto.HeartbeatDto;
import com.ksyun.start.camp.dto.RegisterDto;
import com.ksyun.start.camp.service.LogService;
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
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static java.util.Objects.hash;

/**
 * 服务启动运行逻辑
 */
@Component
@Slf4j
public class ServiceAppRunner implements ApplicationRunner {
    // 从配置文件中获取注册中心的地址
    private   String registryUrl;
    // 获取当前服务的名称
    private  String serviceName;
    // 从配置文件中获取当前服务的主机地址
    private  String serviceHost;
    // 从配置文件中获取当前服务的端口号
    private  int servicePort;
    // 从配置文件中获取日志服务的地址
    private String logUrl;
    //serviceId
    public static  String serviceId = null;

    private RegisterDto registerDto;
    @Autowired
    public ServiceAppRunner(@Value("${spring.application.name}") String serviceName,
                            @Value("${service.host}") String serviceHost,
                            @Value("${server.port}") int servicePort,
                            @Value("${registry.url}") String registryUrl,
                            @Value("${logservice.url}") String logUrl) {
        this.serviceName = serviceName;
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.registryUrl = registryUrl;
        this.logUrl = logUrl;
        serviceId = UUID.randomUUID().toString().substring(0, 8);
        registerDto = new RegisterDto(serviceName, serviceId, serviceHost, servicePort);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 向 registry 服务注册当前服务
        registerService(registerDto);

        // 此处代码会在 Boot 应用启动时执行

        // 开始编写你的逻辑，下面是提示
        // 1. 向 registry 服务注册当前服务
        // 2. 定期发送心跳逻辑
        // 定期发送心跳逻辑
        Timer timer = new Timer();
        timer.schedule(new HeartbeatTask(serviceId), 0, 30000); // 每30秒发送一次心跳
        Timer timer1 = new Timer();// 每1秒发送一次日志
        timer1.schedule(new LogService(serviceName,logUrl), 0, 1000);
//timer.schedule(new LogService(),1000);  // 每1秒发送一次日志
        // TODO
    }

    // 向注册中心注册当前服务
    private void registerService(RegisterDto registerDto) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 构建注册服务的URL
            URL url = new URL(registryUrl + "/api/register");

            //将RegisterDto对象转换成JSON字符串
            String requestBody = ToJsonUtil.toJsonString(registerDto);
            log.info("注册服务的请求体为：{}", requestBody);
            // 创建POST请求
            HttpPost httpPost = new HttpPost(url.toString());
            if (requestBody != null) {
                httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
            }

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
            log.error("注册失败，注册中心不在线");
        }
    }

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
                if (requestBody != null) {
                    httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
                }

                try {
                    // 发送请求并获取响应
                    HttpResponse response = httpClient.execute(httpPost);

                    // 处理响应
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        log.info("心跳发送成功");
                    } else {
                        // 如果心跳发送失败，重新注册服务
                        registerService(registerDto);
                        log.error("心跳发送失败,重新注册服务");
                    }
                }catch (Exception e) {
                    log.error("心跳发送失败，注册中心不在线", e.getMessage());
                }
            } catch (IOException e) {
                log.error("心跳发送失败", e.getMessage());
            }
        }
    }

    // 添加应用生命周期回调，用于应用正常退出时发送注销请求
    @EventListener
    public void onApplicationClosed(ContextClosedEvent event) {
        unregisterService();
    }
    private void unregisterService() {
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 构建注销服务的URL
            URL url = new URL(registryUrl + "/api/unregister");
            RegisterDto registerDto = new RegisterDto(serviceName, serviceId, serviceHost, servicePort);
            //将RegisterDto对象转换成JSON字符串
            String requestBody = ToJsonUtil.toJsonString(registerDto);

            // 创建POST请求
            HttpPost httpPost = new HttpPost(url.toString());
            if (requestBody != null) {
                httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
            }
            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);

            // 处理响应
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("注销成功");
            } else {
                log.info("注销失败");
            }
        } catch (Exception e) {
            log.error("注销失败", e);
        }
    }
}
