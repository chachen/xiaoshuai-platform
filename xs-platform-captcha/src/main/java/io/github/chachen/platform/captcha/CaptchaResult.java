package io.github.chachen.platform.captcha;

public record CaptchaResult(String key, String imageBase64, int expireSeconds) {
}
