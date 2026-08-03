package io.github.chachen.platform.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.chachen.platform.system.entity.SysRole;
import io.github.chachen.platform.system.entity.SysRolePermission;
import io.github.chachen.platform.system.entity.SysUserRole;
import io.github.chachen.platform.system.mapper.SysRoleMapper;
import io.github.chachen.platform.system.mapper.SysRolePermissionMapper;
import io.github.chachen.platform.system.mapper.SysUserRoleMapper;

import java.util.List;
import java.util.Optional;

public class RoleService {
    private final SysRoleMapper mapper;
    private final SysUserRoleMapper userRoles;
    private final SysRolePermissionMapper rolePermissions;

    public RoleService(SysRoleMapper m, SysUserRoleMapper ur, SysRolePermissionMapper rp) {
        mapper = m;
        userRoles = ur;
        rolePermissions = rp;
    }

    public List<RoleView> list() {
        return mapper.selectList(null).stream().map(r -> new RoleView(r.getId(), r.getCode(), r.getName(), r.getStatus())).toList();
    }

    public SysRole create(String code, String name) {
        if (mapper.selectCount(new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, code)) > 0)
            throw new IllegalArgumentException("角色编码已存在");
        var r = new SysRole();
        r.setCode(code);
        r.setName(name);
        r.setStatus(1);
        mapper.insert(r);
        return r;
    }

    public Optional<SysRole> findByCode(String code) {
        return Optional.ofNullable(mapper.selectOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getCode, code).last("limit 1")));
    }

    public void bindUser(Long roleId, Long userId) {
        if (userRoles.selectCount(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId).eq(SysUserRole::getUserId, userId)) == 0) {
            var x = new SysUserRole();
            x.setRoleId(roleId);
            x.setUserId(userId);
            userRoles.insert(x);
        }
    }

    public void bindPermission(Long roleId, Long permissionId) {
        if (rolePermissions.selectCount(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId).eq(SysRolePermission::getPermissionId, permissionId)) == 0) {
            var x = new SysRolePermission();
            x.setRoleId(roleId);
            x.setPermissionId(permissionId);
            rolePermissions.insert(x);
        }
    }

    public record RoleView(Long id, String code, String name, Integer status) {
    }
}
