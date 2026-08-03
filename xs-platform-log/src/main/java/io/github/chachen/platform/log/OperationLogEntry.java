package io.github.chachen.platform.log;
public record OperationLogEntry(String title,String path,String httpMethod,Long userId,String username,String ip,String traceId,long durationMs,boolean success,String errorMessage,String requestParams) {}
