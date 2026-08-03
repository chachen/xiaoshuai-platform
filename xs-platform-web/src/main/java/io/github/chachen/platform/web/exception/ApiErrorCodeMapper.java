package io.github.chachen.platform.web.exception;

@FunctionalInterface
public interface ApiErrorCodeMapper {
    ApiErrorMapping map(String internalCode);
}
