package io.github.chachen.platform.auth;

import io.github.chachen.platform.core.context.CurrentUser;
import io.github.chachen.platform.web.result.ApiResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService s) {
        service = s;
    }

    @PostMapping("/login")
    public ApiResult<AuthService.LoginResult> login(@Valid @RequestBody LoginRequest r) {
        return ApiResult.success(service.login(r.username(), r.password(), r.captchaKey(), r.captchaAnswer()));
    }

    @PostMapping("/refresh")
    public ApiResult<AuthService.LoginResult> refresh(@Valid @RequestBody RefreshRequest r) {
        return ApiResult.success(service.refresh(r.refreshToken()));
    }

    @PostMapping("/logout")
    public ApiResult<Void> logout() {
        CurrentUser.clear();
        return ApiResult.success();
    }

    @GetMapping("/me")
    public ApiResult<CurrentUser> me() {
        return ApiResult.success(CurrentUser.get());
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password, String captchaKey,
                               String captchaAnswer) {
    }

    public record RefreshRequest(@NotBlank String refreshToken) {
    }
}
