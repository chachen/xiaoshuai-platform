package io.github.chachen.platform.web.result;

import io.github.chachen.platform.core.context.TraceContext;
import io.github.chachen.platform.web.exception.CommonErrorCode;
import io.github.chachen.platform.web.exception.DefaultApiErrorCodeMapper;
import io.github.chachen.platform.web.exception.ErrorCode;

/**
 * Platform response envelope. The numeric code and requestId match the shared
 * contract used by downstream applications such as LedgerMind.
 */
public record ApiResult<T>(int code, String message, T data, String requestId) {

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(0, CommonErrorCode.SUCCESS.getMessage(), data, TraceContext.getOrCreate());
    }

    public static ApiResult<Void> success() {
        return success(null);
    }

    public static ApiResult<Void> failure(ErrorCode errorCode) {
        return failure(errorCode.getCode(), errorCode.getMessage());
    }

    public static ApiResult<Void> failure(String code, String message) {
        return failure(new DefaultApiErrorCodeMapper().map(code), message);
    }

    public static ApiResult<Void> failure(int code, String message) {
        return new ApiResult<>(code, message, null, TraceContext.getOrCreate());
    }
}
