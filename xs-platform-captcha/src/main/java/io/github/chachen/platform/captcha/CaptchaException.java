package io.github.chachen.platform.captcha;

public class CaptchaException extends RuntimeException {
    private final String code;

    public CaptchaException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
