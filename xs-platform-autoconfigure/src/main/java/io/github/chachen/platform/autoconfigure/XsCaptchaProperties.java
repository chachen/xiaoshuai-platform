package io.github.chachen.platform.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("xs.captcha")
public class XsCaptchaProperties {
    private boolean enabled = true;
    private int expireSeconds = 120;
    private boolean ignoreCase = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean v) {
        enabled = v;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int v) {
        expireSeconds = v;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean v) {
        ignoreCase = v;
    }
}
