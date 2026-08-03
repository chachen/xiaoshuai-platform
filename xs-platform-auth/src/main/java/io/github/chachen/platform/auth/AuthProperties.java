package io.github.chachen.platform.auth;

import java.util.ArrayList;
import java.util.List;

public class AuthProperties {
    private boolean enabled = true;
    private String secret = "xs-platform-change-this-secret-to-at-least-32-bytes";
    private long accessTtlSeconds = 7200;
    private long refreshTtlSeconds = 2592000;
    private boolean captchaRequired = false;
    private boolean securityChainEnabled = true;
    private List<String> permitPaths = new ArrayList<>(List.of("/api/auth/login", "/api/auth/refresh", "/api/captcha/**", "/error", "/test/**"));

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean v) {
        enabled = v;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String v) {
        secret = v;
    }

    public long getAccessTtlSeconds() {
        return accessTtlSeconds;
    }

    public void setAccessTtlSeconds(long v) {
        accessTtlSeconds = v;
    }

    public long getRefreshTtlSeconds() {
        return refreshTtlSeconds;
    }

    public void setRefreshTtlSeconds(long v) {
        refreshTtlSeconds = v;
    }

    public boolean isCaptchaRequired() {
        return captchaRequired;
    }

    public void setCaptchaRequired(boolean v) {
        captchaRequired = v;
    }

    public boolean isSecurityChainEnabled() {
        return securityChainEnabled;
    }

    public void setSecurityChainEnabled(boolean v) {
        securityChainEnabled = v;
    }

    public List<String> getPermitPaths() {
        return permitPaths;
    }

    public void setPermitPaths(List<String> v) {
        permitPaths = v;
    }
}
