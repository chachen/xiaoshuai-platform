package io.github.chachen.platform.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("xs.system")
public class XsSystemProperties {
    private boolean enabled = true;
    private boolean initAdmin = false;
    private String adminUsername = "admin";
    private String adminPassword = "ChangeMe_123456";
    private String adminNickname = "平台管理员";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean v) {
        enabled = v;
    }

    public boolean isInitAdmin() {
        return initAdmin;
    }

    public void setInitAdmin(boolean v) {
        initAdmin = v;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String v) {
        adminUsername = v;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String v) {
        adminPassword = v;
    }

    public String getAdminNickname() {
        return adminNickname;
    }

    public void setAdminNickname(String v) {
        adminNickname = v;
    }
}
