package io.github.chachen.platform.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("xs.file")
public class FileProperties {
    private boolean enabled = true;
    private String backend = "local";
    private String root = "./data/files";
    private long maxSize = 10485760;
    private String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "pdf", "txt", "zip"};
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket = "platform";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean v) {
        enabled = v;
    }

    public String getBackend() { return backend; }
    public void setBackend(String v) { backend = v; }

    public String getRoot() {
        return root;
    }

    public void setRoot(String v) {
        root = v;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long v) {
        maxSize = v;
    }

    public String[] getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(String[] v) {
        allowedExtensions = v;
    }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String v) { endpoint = v; }
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String v) { accessKey = v; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String v) { secretKey = v; }
    public String getBucket() { return bucket; }
    public void setBucket(String v) { bucket = v; }
}
