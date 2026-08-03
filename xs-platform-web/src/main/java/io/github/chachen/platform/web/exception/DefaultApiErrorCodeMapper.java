package io.github.chachen.platform.web.exception;

public final class DefaultApiErrorCodeMapper implements ApiErrorCodeMapper {
    @Override
    public int map(String code) {
        try {
            return Integer.parseInt(code);
        } catch (NumberFormatException ignored) {
            return switch (code) {
                case "COMMON_UNAUTHORIZED" -> 401;
                case "COMMON_FORBIDDEN" -> 403;
                case "COMMON_NOT_FOUND" -> 404;
                case "COMMON_METHOD_NOT_ALLOWED" -> 405;
                case "COMMON_INTERNAL_ERROR" -> 500;
                default -> 400;
            };
        }
    }
}
