package io.github.chachen.platform.auth;
import java.lang.annotation.*;
@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME) public @interface RequirePermission { String value(); }
