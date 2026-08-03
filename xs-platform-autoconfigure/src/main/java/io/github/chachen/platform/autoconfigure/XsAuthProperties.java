package io.github.chachen.platform.autoconfigure;

import io.github.chachen.platform.auth.AuthProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("xs.auth")
public class XsAuthProperties extends AuthProperties {
}
