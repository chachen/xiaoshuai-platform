package io.github.chachen.platform.web.exception;

import io.github.chachen.platform.web.result.ApiResult;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ApiErrorCodeMapper errorCodeMapper;

    public GlobalExceptionHandler(ApiErrorCodeMapper errorCodeMapper) {
        this.errorCodeMapper = errorCodeMapper;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResult<Void>> handleBusinessException(BusinessException exception) {
        return failure(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Void>> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse(CommonErrorCode.INVALID_PARAMETER.getMessage());
        return failure(CommonErrorCode.INVALID_PARAMETER.getCode(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResult<Void>> handleConstraintViolation(ConstraintViolationException exception) {
        return failure(CommonErrorCode.INVALID_PARAMETER.getCode(), exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResult<Void>> handleJsonException() {
        return failure(CommonErrorCode.INVALID_PARAMETER.getCode(), "请求体格式错误");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResult<Void>> handleMethodException() {
        return failure(CommonErrorCode.METHOD_NOT_ALLOWED.getCode(), CommonErrorCode.METHOD_NOT_ALLOWED.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResult<Void>> handleNotFound() {
        return failure(CommonErrorCode.NOT_FOUND.getCode(), CommonErrorCode.NOT_FOUND.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleException(Exception exception) {
        log.error("Unhandled application exception", exception);
        return failure(CommonErrorCode.INTERNAL_ERROR.getCode(), CommonErrorCode.INTERNAL_ERROR.getMessage());
    }

    private ResponseEntity<ApiResult<Void>> failure(String internalCode, String message) {
        ApiErrorMapping mapping = errorCodeMapper.map(internalCode);
        return ResponseEntity.status(mapping.httpStatus()).body(ApiResult.failure(mapping.code(), message));
    }
}
