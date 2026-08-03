package io.github.chachen.platform.web.exception;

import org.springframework.http.HttpStatus;

public record ApiErrorMapping(String code, HttpStatus httpStatus) {
}
