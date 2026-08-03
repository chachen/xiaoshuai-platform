package io.github.chachen.platform.web.exception;

public enum CommonErrorCode implements ErrorCode {

    SUCCESS("0", "success"),
    INVALID_PARAMETER("COMMON_INVALID_PARAMETER", "请求参数错误"),
    UNAUTHORIZED("COMMON_UNAUTHORIZED", "未登录或登录已过期"),
    FORBIDDEN("COMMON_FORBIDDEN", "无权访问"),
    NOT_FOUND("COMMON_NOT_FOUND", "资源不存在"),
    METHOD_NOT_ALLOWED("COMMON_METHOD_NOT_ALLOWED", "请求方法不支持"),
    INTERNAL_ERROR("COMMON_INTERNAL_ERROR", "系统内部错误");

    private final String code;
    private final String message;

    CommonErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
