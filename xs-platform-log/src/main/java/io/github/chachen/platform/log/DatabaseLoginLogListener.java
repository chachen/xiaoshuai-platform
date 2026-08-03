package io.github.chachen.platform.log;
import io.github.chachen.platform.auth.LoginEvent; import org.springframework.context.event.EventListener; import org.springframework.scheduling.annotation.Async;
public class DatabaseLoginLogListener {
    private final PlatformLoginLogMapper mapper; public DatabaseLoginLogListener(PlatformLoginLogMapper mapper){this.mapper=mapper;}
    @Async @EventListener public void onLogin(LoginEvent e){var x=new PlatformLoginLog();x.setUsername(e.username());x.setStatus(e.status());x.setMessage(e.message());x.setIp(e.ip());x.setUserAgent(e.userAgent());x.setTraceId(e.traceId());x.setCreateTime(e.time());mapper.insert(x);}
}
