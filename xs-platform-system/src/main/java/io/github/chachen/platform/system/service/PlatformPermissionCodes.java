package io.github.chachen.platform.system.service;

import java.util.Map;

public final class PlatformPermissionCodes {
    public static final String ADMIN_ROLE = "xs:platform-admin";
    public static final Map<String, String> ADMIN_PERMISSIONS = Map.of(
            "xs:system:user:read", "查看用户",
            "xs:system:user:create", "创建用户",
            "xs:system:user:update", "修改用户状态",
            "xs:system:role:read", "查看角色",
            "xs:system:role:create", "创建角色",
            "xs:system:role:bind", "绑定角色权限",
            "xs:system:permission:read", "查看权限",
            "xs:system:permission:create", "创建权限",
            "xs:system:menu:read", "查看菜单",
            "xs:system:menu:create", "创建菜单"
    );

    private PlatformPermissionCodes() {
    }
}
