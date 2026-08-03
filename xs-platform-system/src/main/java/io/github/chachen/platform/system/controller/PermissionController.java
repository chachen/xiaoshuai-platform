package io.github.chachen.platform.system.controller;

import io.github.chachen.platform.auth.RequirePermission;
import io.github.chachen.platform.system.service.PermissionService;
import io.github.chachen.platform.web.result.ApiResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/permissions")
public class PermissionController {
    private final PermissionService service;

    public PermissionController(PermissionService s) {
        service = s;
    }

    @GetMapping
    @RequirePermission("xs:system:permission:read")
    public ApiResult<List<PermissionService.PermissionView>> list() {
        return ApiResult.success(service.list());
    }

    @PostMapping
    @RequirePermission("xs:system:permission:create")
    public ApiResult<Void> create(@Valid @RequestBody CreateRequest r) {
        service.create(r.code(), r.name());
        return ApiResult.success();
    }

    public record CreateRequest(@NotBlank String code, @NotBlank String name) {
    }
}
