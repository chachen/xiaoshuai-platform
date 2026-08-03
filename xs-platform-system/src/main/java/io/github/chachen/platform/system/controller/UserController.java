package io.github.chachen.platform.system.controller;

import io.github.chachen.platform.auth.RequirePermission;
import io.github.chachen.platform.system.service.UserService;
import io.github.chachen.platform.web.result.ApiResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("xs:system:user:read")
    public ApiResult<List<UserService.UserView>> list() {
        return ApiResult.success(service.list());
    }

    @PostMapping
    @RequirePermission("xs:system:user:create")
    public ApiResult<Void> create(@Valid @RequestBody CreateRequest req) {
        service.create(req.username(), req.password(), req.nickname());
        return ApiResult.success();
    }

    @PutMapping("/{id}/status")
    @RequirePermission("xs:system:user:update")
    public ApiResult<Void> status(@PathVariable Long id, @RequestParam boolean enabled) {
        service.setStatus(id, enabled);
        return ApiResult.success();
    }

    public record CreateRequest(@NotBlank @Size(max = 50) String username,
                                @NotBlank @Size(min = 8, max = 100) String password, @Size(max = 100) String nickname) {
    }
}
