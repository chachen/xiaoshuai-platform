package io.github.chachen.platform.system.controller;

import io.github.chachen.platform.auth.RequirePermission;
import io.github.chachen.platform.system.service.RoleService;
import io.github.chachen.platform.web.result.ApiResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/roles")
public class RoleController {
    private final RoleService service;

    public RoleController(RoleService s) {
        service = s;
    }

    @GetMapping
    @RequirePermission("xs:system:role:read")
    public ApiResult<List<RoleService.RoleView>> list() {
        return ApiResult.success(service.list());
    }

    @PostMapping
    @RequirePermission("xs:system:role:create")
    public ApiResult<Void> create(@Valid @RequestBody CreateRequest r) {
        service.create(r.code(), r.name());
        return ApiResult.success();
    }

    @PutMapping("/{roleId}/users/{userId}")
    @RequirePermission("xs:system:role:bind")
    public ApiResult<Void> bindUser(@PathVariable Long roleId, @PathVariable Long userId) {
        service.bindUser(roleId, userId);
        return ApiResult.success();
    }

    @PutMapping("/{roleId}/permissions/{permissionId}")
    @RequirePermission("xs:system:role:bind")
    public ApiResult<Void> bindPermission(@PathVariable Long roleId, @PathVariable Long permissionId) {
        service.bindPermission(roleId, permissionId);
        return ApiResult.success();
    }

    public record CreateRequest(@NotBlank String code, @NotBlank String name) {
    }
}
