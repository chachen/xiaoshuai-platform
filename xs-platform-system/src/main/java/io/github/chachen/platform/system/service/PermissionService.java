package io.github.chachen.platform.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.chachen.platform.system.entity.SysPermission;
import io.github.chachen.platform.system.mapper.SysPermissionMapper;

import java.util.List;
import java.util.Optional;

public class PermissionService {
    private final SysPermissionMapper mapper;

    public PermissionService(SysPermissionMapper m) {
        mapper = m;
    }

    public List<PermissionView> list() {
        return mapper.selectList(null).stream().map(p -> new PermissionView(p.getId(), p.getCode(), p.getName(), p.getStatus())).toList();
    }

    public SysPermission create(String code, String name) {
        if (mapper.selectCount(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getCode, code)) > 0)
            throw new IllegalArgumentException("权限编码已存在");
        var p = new SysPermission();
        p.setCode(code);
        p.setName(name);
        p.setStatus(1);
        mapper.insert(p);
        return p;
    }

    public Optional<SysPermission> findByCode(String code) {
        return Optional.ofNullable(mapper.selectOne(new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getCode, code).last("limit 1")));
    }

    public record PermissionView(Long id, String code, String name, Integer status) {
    }
}
