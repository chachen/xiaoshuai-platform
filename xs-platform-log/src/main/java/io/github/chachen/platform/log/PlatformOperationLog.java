package io.github.chachen.platform.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("xs_operation_log")
public class PlatformOperationLog {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String title;
    private String path;
    private String httpMethod;
    private Long userId;
    private String username;
    private String ip;
    private String traceId;
    private Long durationMs;
    private Integer success;
    private String errorMessage;
    private String requestParams;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long v) { id = v; }
    public String getTitle() { return title; }
    public void setTitle(String v) { title = v; }
    public String getPath() { return path; }
    public void setPath(String v) { path = v; }
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String v) { httpMethod = v; }
    public Long getUserId() { return userId; }
    public void setUserId(Long v) { userId = v; }
    public String getUsername() { return username; }
    public void setUsername(String v) { username = v; }
    public String getIp() { return ip; }
    public void setIp(String v) { ip = v; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String v) { traceId = v; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long v) { durationMs = v; }
    public Integer getSuccess() { return success; }
    public void setSuccess(Integer v) { success = v; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String v) { errorMessage = v; }
    public String getRequestParams() { return requestParams; }
    public void setRequestParams(String v) { requestParams = v; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime v) { createTime = v; }
}
