package io.github.chachen.platform.auth;
import java.time.LocalDateTime;
public record LoginEvent(String username,String status,String message,String ip,String userAgent,String traceId,LocalDateTime time) {}
