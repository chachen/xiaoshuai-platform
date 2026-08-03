package io.github.chachen.platform.autoconfigure;
import io.github.chachen.platform.captcha.*; import org.springframework.boot.autoconfigure.*; import org.springframework.boot.autoconfigure.condition.*; import org.springframework.context.annotation.Bean; import org.springframework.data.redis.core.StringRedisTemplate;
@AutoConfiguration(after=XsCaptchaAutoConfiguration.class) @ConditionalOnProperty(prefix="xs.captcha",name="enabled",havingValue="true",matchIfMissing=false) @ConditionalOnClass(name="org.springframework.data.redis.core.StringRedisTemplate")
public class XsRedisCaptchaAutoConfiguration { @Bean @ConditionalOnMissingBean(CaptchaStore.class) CaptchaStore redisCaptchaStore(StringRedisTemplate redis){return new RedisCaptchaStore(redis);} }
