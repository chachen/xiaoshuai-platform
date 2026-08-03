package io.github.chachen.platform.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("xs_sys_role_permission")
public class SysRolePermission {
    @TableId(type = IdType.INPUT)
    private Long roleId;
    private Long permissionId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long v) {
        roleId = v;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long v) {
        permissionId = v;
    }
}
