package io.github.chachen.platform.autoconfigure;

import io.github.chachen.platform.system.config.SystemConfiguration;
import io.github.chachen.platform.system.controller.PermissionController;
import io.github.chachen.platform.system.controller.RoleController;
import io.github.chachen.platform.system.controller.UserController;
import io.github.chachen.platform.system.controller.MenuController;
import io.github.chachen.platform.system.service.UserService;
import io.github.chachen.platform.system.service.PermissionService;
import io.github.chachen.platform.system.service.PlatformPermissionCodes;
import io.github.chachen.platform.system.service.RoleService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@AutoConfiguration(after = XsDatabaseAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xs.system", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(XsSystemProperties.class)
@Import({SystemConfiguration.class, UserController.class, RoleController.class, PermissionController.class, MenuController.class})
public class XsSystemAutoConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "xs.system", name = "init-admin", havingValue = "true")
    ApplicationRunner initAdmin(UserService users, RoleService roles, PermissionService permissions, XsSystemProperties p) {
        return args -> {
            try {
                if (users.findByUsername(p.getAdminUsername()).isEmpty())
                    users.create(p.getAdminUsername(), p.getAdminPassword(), p.getAdminNickname());
                var admin = users.findByUsername(p.getAdminUsername())
                        .orElseThrow(() -> new IllegalStateException("管理员初始化后无法读取账号"));
                var role = roles.findByCode(PlatformPermissionCodes.ADMIN_ROLE)
                        .orElseGet(() -> roles.create(PlatformPermissionCodes.ADMIN_ROLE, "平台管理员"));
                PlatformPermissionCodes.ADMIN_PERMISSIONS.forEach((code, name) -> {
                    var permission = permissions.findByCode(code)
                            .orElseGet(() -> permissions.create(code, name));
                    roles.bindPermission(role.getId(), permission.getId());
                });
                roles.bindUser(role.getId(), admin.id());
            } catch (Exception e) {
                throw new IllegalStateException("默认管理员初始化失败", e);
            }
        };
    }
}
