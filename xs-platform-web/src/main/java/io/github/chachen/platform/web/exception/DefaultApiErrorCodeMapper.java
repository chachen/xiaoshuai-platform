package io.github.chachen.platform.web.exception;

import org.springframework.http.HttpStatus;

public final class DefaultApiErrorCodeMapper implements ApiErrorCodeMapper {
    @Override
    public ApiErrorMapping map(String code) {
        HttpStatus status = switch (code) {
            case "COMMON_UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED;
            case "COMMON_FORBIDDEN" -> HttpStatus.FORBIDDEN;
            case "COMMON_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "COMMON_METHOD_NOT_ALLOWED" -> HttpStatus.METHOD_NOT_ALLOWED;
            case "COMMON_INTERNAL_ERROR" -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.BAD_REQUEST;
        };
        return new ApiErrorMapping(code, status);
    }
}
