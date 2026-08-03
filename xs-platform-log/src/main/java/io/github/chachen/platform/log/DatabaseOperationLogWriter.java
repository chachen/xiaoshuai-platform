package io.github.chachen.platform.log;
import org.springframework.scheduling.annotation.Async;
public class DatabaseOperationLogWriter implements OperationLogWriter {
    private final PlatformOperationLogMapper mapper; public DatabaseOperationLogWriter(PlatformOperationLogMapper mapper){this.mapper=mapper;}
    @Async @Override public void write(OperationLogEntry e){var x=new PlatformOperationLog();x.setTitle(e.title());x.setPath(e.path());x.setHttpMethod(e.httpMethod());x.setUserId(e.userId());x.setUsername(e.username());x.setIp(e.ip());x.setTraceId(e.traceId());x.setDurationMs(e.durationMs());x.setSuccess(e.success()?1:0);x.setErrorMessage(e.errorMessage());x.setRequestParams(e.requestParams());x.setCreateTime(java.time.LocalDateTime.now());mapper.insert(x);}
}
