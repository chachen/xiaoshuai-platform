package io.github.chachen.platform.testapp.controller;

import io.github.chachen.platform.web.exception.BusinessException;
import io.github.chachen.platform.web.result.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/success")
    public ApiResult<Map<String, String>> success() {
        return ApiResult.success(Map.of("message", "platform web works"));
    }

    @GetMapping("/business-error")
    public ApiResult<Void> businessError() {
        throw new BusinessException(
                "TEST_BUSINESS_ERROR",
                "测试业务异常"
        );
    }

    @GetMapping("/system-error")
    public ApiResult<Void> systemError() {
        throw new IllegalStateException("测试系统异常");
    }
}
