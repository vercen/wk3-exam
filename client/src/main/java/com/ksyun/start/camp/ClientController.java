package com.ksyun.start.camp;

import com.ksyun.start.camp.service.ClientService;
import com.ksyun.start.camp.utils.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认的客户端 API Controller
 */
@RestController
public class ClientController {
    @Autowired
    private ClientService clientService;
    // 获取客户端信息接口
    @GetMapping("/api/getInfo")
    public Object getInfo() {
        try {
            String info = clientService.getInfo();
//            ApiResponse apiResponse = new ApiResponse(null, info);
            return ApiResponse.success(info);
//            return RespBean.success(apiResponse);
        } catch (Exception e) {
//            ApiResponse apiResponse = new ApiResponse("授时服务器不存在", null);
            return ApiResponse.error("授时服务器不存在");
//            return RespBean.error(apiResponse);
        }
    }

    // 在这里开始编写你的相关接口实现代码
    // 返回值对象使用 ApiResponse 类

    // 提示：调用 ClientService
}
