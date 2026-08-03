package io.github.chachen.platform.web.result;

import io.github.chachen.platform.core.context.TraceContext;
import io.github.chachen.platform.web.exception.CommonErrorCode;
import io.github.chachen.platform.web.exception.ErrorCode;

/**
 * Success uses numeric code 0; failures preserve the string business code.
 * The code is Object only because the public JSON contract intentionally uses
 * different JSON scalar types for success and failure.
 */
public record ApiResult<T>(Object code, String message, T data, String requestId) {

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
        return new ApiResult<>(code, message, null, TraceContext.getOrCreate());
    }
}
