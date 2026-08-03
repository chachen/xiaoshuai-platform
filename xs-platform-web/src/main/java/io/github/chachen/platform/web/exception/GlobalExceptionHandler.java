package io.github.chachen.platform.web.exception;

import io.github.chachen.platform.web.result.ApiResult;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ApiErrorCodeMapper errorCodeMapper;

    public GlobalExceptionHandler(ApiErrorCodeMapper errorCodeMapper) {
        this.errorCodeMapper = errorCodeMapper;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResult<Void>> handleBusinessException(
        BusinessException exception
    ) {
        HttpStatus status = CommonErrorCode.FORBIDDEN.getCode().equals(exception.getCode())
            ? HttpStatus.FORBIDDEN : HttpStatus.BAD_REQUEST;
        return ResponseEntity
            .status(status)
            .body(ApiResult.failure(
                    errorCodeMapper.map(exception.getCode()),
                    exception.getMessage()
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Void>> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse(CommonErrorCode.INVALID_PARAMETER.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResult.failure(
                        errorCodeMapper.map(CommonErrorCode.INVALID_PARAMETER.getCode()),
                        message
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResult<Void>> handleConstraintViolation(
            ConstraintViolationException exception
    ) {
        return ResponseEntity
                .badRequest()
                .body(ApiResult.failure(
                        errorCodeMapper.map(CommonErrorCode.INVALID_PARAMETER.getCode()),
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResult<Void>> handleJsonException() {
        return ResponseEntity.badRequest().body(ApiResult.failure(errorCodeMapper.map(CommonErrorCode.INVALID_PARAMETER.getCode()), "请求体格式错误"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResult<Void>> handleMethodException() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ApiResult.failure(CommonErrorCode.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResult<Void>> handleNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResult.failure(CommonErrorCode.NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleException(Exception exception) {
        log.error("Unhandled application exception", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.failure(CommonErrorCode.INTERNAL_ERROR));
    }
}
