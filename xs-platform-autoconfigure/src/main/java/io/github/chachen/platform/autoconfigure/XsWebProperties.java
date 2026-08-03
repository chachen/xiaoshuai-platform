package io.github.chachen.platform.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("xs.web")
public class XsWebProperties {
    private boolean globalErrorHandlerEnabled = false;

    public boolean isGlobalErrorHandlerEnabled() {
        return globalErrorHandlerEnabled;
    }

    public void setGlobalErrorHandlerEnabled(boolean v) {
        globalErrorHandlerEnabled = v;
    }
}
