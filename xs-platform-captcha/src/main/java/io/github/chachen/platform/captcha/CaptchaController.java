package io.github.chachen.platform.captcha;

import io.github.chachen.platform.web.result.ApiResult;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/captcha")
@Validated
public class CaptchaController {
    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping
    public ApiResult<CaptchaResult> generate() {
        return ApiResult.success(captchaService.generate());
    }

    @PostMapping("/verify")
    public ApiResult<Void> verify(@RequestParam @NotBlank String key, @RequestParam @NotBlank String answer) {
        captchaService.verify(key, answer);
        return ApiResult.success();
    }
}
