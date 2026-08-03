package io.github.chachen.platform.captcha;

public interface CaptchaService {
    CaptchaResult generate();

    void verify(String captchaKey, String answer);
}
