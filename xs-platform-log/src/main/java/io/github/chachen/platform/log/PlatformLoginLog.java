package io.github.chachen.platform.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("xs_login_log")
public class PlatformLoginLog {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String username;
    private String status;
    private String message;
    private String ip;
    private String userAgent;
    private String traceId;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long v) { id = v; }
    public String getUsername() { return username; }
    public void setUsername(String v) { username = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public String getMessage() { return message; }
    public void setMessage(String v) { message = v; }
    public String getIp() { return ip; }
    public void setIp(String v) { ip = v; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String v) { userAgent = v; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String v) { traceId = v; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime v) { createTime = v; }
}
