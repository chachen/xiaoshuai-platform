package io.github.chachen.platform.auth;

import io.github.chachen.platform.captcha.CaptchaService;
import io.github.chachen.platform.core.auth.Account;
import io.github.chachen.platform.core.auth.AccountProvider;
import io.github.chachen.platform.core.context.TraceContext;
import io.github.chachen.platform.web.exception.BusinessException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthService {
    private final AccountProvider accounts;
    private final PasswordEncoder encoder;
    private final JwtTokenService tokens;
    private final CaptchaService captcha;
    private final AuthProperties properties;
    private final ApplicationEventPublisher events;
    private final Map<String, Failure> failures = new ConcurrentHashMap<>();

    public AuthService(AccountProvider a, PasswordEncoder e, JwtTokenService t, CaptchaService c, AuthProperties p, ApplicationEventPublisher events) {
        accounts = a;
        encoder = e;
        tokens = t;
        captcha = c;
        properties = p;
        this.events = events;
    }

    public LoginResult login(String username, String password, String captchaKey, String captchaAnswer) {
        if (properties.isCaptchaRequired()) {
            if (captcha == null) throw new BusinessException("CAPTCHA_UNAVAILABLE", "验证码服务未启用");
            captcha.verify(captchaKey, captchaAnswer);
        }
        Failure failure = failures.get(username);
        if (failure != null && failure.lockedUntil() > System.currentTimeMillis())
            throw new BusinessException("AUTH_TOO_MANY_ATTEMPTS", "登录失败次数过多，请稍后再试");
        Account a = accounts.findByUsername(username).orElseThrow(() -> {
            recordFailure(username);
            publish(username, "1", "用户名或密码错误");
            return new BusinessException("AUTH_INVALID_CREDENTIALS", "用户名或密码错误");
        });
        if (!a.canLogin()) {
            publish(username, "1", "账号已禁用或锁定");
            throw new BusinessException("AUTH_ACCOUNT_DISABLED", "账号已禁用或锁定");
        }
        if (!encoder.matches(password, a.passwordHash())) {
            recordFailure(username);
            publish(username, "1", "用户名或密码错误");
            throw new BusinessException("AUTH_INVALID_CREDENTIALS", "用户名或密码错误");
        }
        failures.remove(username);
        publish(username, "0", "登录成功");
        return new LoginResult(tokens.accessToken(a), tokens.refreshToken(a), a.id(), a.username());
    }

    private void publish(String username, String status, String message) {
        var attrs = RequestContextHolder.getRequestAttributes();
        String ip = "";
        String userAgent = "";
        if (attrs instanceof ServletRequestAttributes servlet) {
            ip = servlet.getRequest().getRemoteAddr();
            userAgent = servlet.getRequest().getHeader("User-Agent");
        }
        events.publishEvent(new LoginEvent(username, status, message, ip, userAgent, TraceContext.get(), LocalDateTime.now()));
    }

    private void recordFailure(String username) {
        failures.compute(username, (k, v) -> {
            var f = v == null ? new Failure(new AtomicInteger(), 0) : v;
            int count = f.count().incrementAndGet();
            return count >= 5 ? new Failure(f.count(), System.currentTimeMillis() + 15 * 60_000L) : f;
        });
    }

    public LoginResult refresh(String refreshToken) {
        try {
            var claims = tokens.parse(refreshToken);
            if (!tokens.isRefresh(claims)) throw new Exception();
            String username = claims.getSubject();
            Account a = accounts.findByUsername(username).orElseThrow();
            if (!a.canLogin()) throw new Exception();
            return new LoginResult(tokens.accessToken(a), tokens.refreshToken(a), a.id(), a.username());
        } catch (Exception e) {
            throw new BusinessException("AUTH_REFRESH_INVALID", "刷新令牌无效或已过期");
        }
    }

    private record Failure(AtomicInteger count, long lockedUntil) {
    }

    public record LoginResult(String accessToken, String refreshToken, Long userId, String username) {
    }
}
