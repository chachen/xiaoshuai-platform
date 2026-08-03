package io.github.chachen.platform.web.exception;

@FunctionalInterface
public interface ApiErrorCodeMapper {
    int map(String internalCode);
}
