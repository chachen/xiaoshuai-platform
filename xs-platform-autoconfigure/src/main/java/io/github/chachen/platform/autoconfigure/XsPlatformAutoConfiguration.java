package io.github.chachen.platform.autoconfigure;

import io.github.chachen.platform.core.XsPlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(XsPlatformProperties.class)
@ConditionalOnProperty(
    prefix = "xs.platform",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class XsPlatformAutoConfiguration {

    private static final Logger log =
        LoggerFactory.getLogger(XsPlatformAutoConfiguration.class);

    public XsPlatformAutoConfiguration(XsPlatformProperties properties) {
        log.info("{} enabled for application: {}",
            XsPlatform.NAME,
            properties.getApplicationName());
    }
}
