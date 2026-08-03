package io.github.chachen.platform.captcha;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCaptchaStore implements CaptchaStore {
    private final ConcurrentHashMap<String, Entry> values = new ConcurrentHashMap<>();

    @Override
    public void save(String key, String value, Duration ttl) {
        values.put(key, new Entry(value, System.currentTimeMillis() + ttl.toMillis()));
    }

    @Override
    public String get(String key) {
        Entry e = values.get(key);
        if (e == null) return null;
        if (e.expiresAt() < System.currentTimeMillis()) {
            values.remove(key, e);
            return null;
        }
        return e.value();
    }

    @Override
    public void delete(String key) {
        values.remove(key);
    }

    private record Entry(String value, long expiresAt) {
    }
}
