package io.github.chachen.platform.captcha;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.time.Duration;
public class RedisCaptchaStore implements CaptchaStore {
    private final StringRedisTemplate redis;
    public RedisCaptchaStore(StringRedisTemplate redis){this.redis=redis;}
    private String key(String key){return "xs:captcha:"+key;}
    @Override public void save(String key,String value,Duration ttl){redis.opsForValue().set(key(key),value,ttl);}
    @Override public String get(String key){return key==null?null:redis.opsForValue().get(key(key));}
    @Override public void delete(String key){if(key!=null)redis.delete(key(key));}
}
