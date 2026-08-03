package io.github.chachen.platform.system.controller;

import io.github.chachen.platform.auth.RequirePermission;
import io.github.chachen.platform.system.service.MenuService;
import io.github.chachen.platform.web.result.ApiResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/menus")
public class MenuController {
    private final MenuService service;

    public MenuController(MenuService s) {
        service = s;
    }

    @GetMapping
    @RequirePermission("xs:system:menu:read")
    public ApiResult<List<MenuService.MenuView>> list() {
        return ApiResult.success(service.list());
    }

    @PostMapping
    @RequirePermission("xs:system:menu:create")
    public ApiResult<Void> create(@Valid @RequestBody CreateRequest r) {
        service.create(r.parentId(), r.name(), r.path(), r.permission(), r.sort());
        return ApiResult.success();
    }

    public record CreateRequest(Long parentId, @NotBlank String name, String path, String permission, Integer sort) {
    }
}
