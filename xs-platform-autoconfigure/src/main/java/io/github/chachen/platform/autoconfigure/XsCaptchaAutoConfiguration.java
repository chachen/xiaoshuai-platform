package io.github.chachen.platform.autoconfigure;

import io.github.chachen.platform.captcha.*;
import io.github.chachen.platform.web.exception.ApiErrorCodeMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration(after = XsWebAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xs.captcha", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(XsCaptchaProperties.class)
@Import(CaptchaController.class)
public class XsCaptchaAutoConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "xs.web", name = "global-error-handler-enabled", havingValue = "true")
    @ConditionalOnMissingBean(CaptchaExceptionHandler.class)
    CaptchaExceptionHandler captchaExceptionHandler(ApiErrorCodeMapper mapper) {
        return new CaptchaExceptionHandler(mapper);
    }

    @Bean
    @ConditionalOnMissingBean(CaptchaStore.class)
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass("org.springframework.data.redis.core.StringRedisTemplate")
    CaptchaStore captchaStore() {
        return new InMemoryCaptchaStore();
    }

    @Bean
    @ConditionalOnMissingBean
    CaptchaService captchaService(CaptchaStore s, XsCaptchaProperties p) {
        return new DefaultCaptchaService(s, p.getExpireSeconds(), p.isIgnoreCase());
    }
}
