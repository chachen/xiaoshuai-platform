package io.github.chachen.platform.system.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.github.chachen.platform.system.mapper.SysUserMapper;
import io.github.chachen.platform.system.service.PermissionService;
import io.github.chachen.platform.system.service.RoleService;
import io.github.chachen.platform.system.service.UserService;
import io.github.chachen.platform.system.service.MenuService;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@MapperScan(basePackageClasses = SysUserMapper.class)
public class SystemConfiguration {
    @Bean
    @ConditionalOnMissingBean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserService userService(SysUserMapper mapper, PasswordEncoder encoder) {
        return new UserService(mapper, encoder);
    }

    @Bean
    RoleService roleService(io.github.chachen.platform.system.mapper.SysRoleMapper mapper, io.github.chachen.platform.system.mapper.SysUserRoleMapper userRoles, io.github.chachen.platform.system.mapper.SysRolePermissionMapper rolePermissions) {
        return new RoleService(mapper, userRoles, rolePermissions);
    }

    @Bean
    PermissionService permissionService(io.github.chachen.platform.system.mapper.SysPermissionMapper mapper) {
        return new PermissionService(mapper);
    }

    @Bean
    MenuService menuService(io.github.chachen.platform.system.mapper.SysMenuMapper mapper) {
        return new MenuService(mapper);
    }

    @Bean
    MetaObjectHandler xsMetaObjectHandler() {
        return new MetaObjectHandler() {
            public void insertFill(MetaObject m) {
                strictInsertFill(m, "createTime", LocalDateTime.class, LocalDateTime.now());
                strictInsertFill(m, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }

            public void updateFill(MetaObject m) {
                strictUpdateFill(m, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}
