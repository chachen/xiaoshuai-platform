package io.github.chachen.platform.captcha;

import io.github.chachen.platform.web.exception.ApiErrorCodeMapper;
import io.github.chachen.platform.web.exception.ApiErrorMapping;
import io.github.chachen.platform.web.result.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CaptchaExceptionHandler {
    private final ApiErrorCodeMapper errorCodeMapper;

    public CaptchaExceptionHandler(ApiErrorCodeMapper errorCodeMapper) {
        this.errorCodeMapper = errorCodeMapper;
    }

    @ExceptionHandler(CaptchaException.class)
    public ResponseEntity<ApiResult<Void>> handle(CaptchaException exception) {
        ApiErrorMapping mapping = errorCodeMapper.map(exception.getCode());
        return ResponseEntity.status(mapping.httpStatus())
                .body(ApiResult.failure(mapping.code(), exception.getMessage()));
    }
}
