package io.github.chachen.platform.autoconfigure;

import io.github.chachen.platform.auth.AuthConfiguration;
import io.github.chachen.platform.auth.AuthController;
import io.github.chachen.platform.core.auth.AccountProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration(after = {XsCaptchaAutoConfiguration.class, XsSystemAutoConfiguration.class})
@ConditionalOnProperty(prefix = "xs.auth", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnBean(AccountProvider.class)
@ConditionalOnClass(name = "org.springframework.security.web.SecurityFilterChain")
@EnableConfigurationProperties(XsAuthProperties.class)
@Import({AuthConfiguration.class, AuthController.class})
public class XsAuthAutoConfiguration {
}
