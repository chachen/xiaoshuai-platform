package io.github.chachen.platform.autoconfigure;

import io.github.chachen.platform.web.exception.GlobalExceptionHandler;
import io.github.chachen.platform.web.exception.ApiErrorCodeMapper;
import io.github.chachen.platform.web.exception.DefaultApiErrorCodeMapper;
import io.github.chachen.platform.web.filter.CurrentUserCleanupFilter;
import io.github.chachen.platform.web.filter.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration(after = XsPlatformAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(
        prefix = "xs.web",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(XsWebProperties.class)
public class XsWebAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(ApiErrorCodeMapper.class)
    public ApiErrorCodeMapper xsApiErrorCodeMapper() {
        return new DefaultApiErrorCodeMapper();
    }

    @Bean
    @ConditionalOnProperty(prefix = "xs.web", name = "global-error-handler-enabled", havingValue = "true")
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler xsGlobalExceptionHandler(ApiErrorCodeMapper mapper) {
        return new GlobalExceptionHandler(mapper);
    }

    @Bean
    public TraceIdFilter xsTraceIdFilter() {
        return new TraceIdFilter();
    }

    @Bean
    public CurrentUserCleanupFilter xsCurrentUserCleanupFilter() {
        return new CurrentUserCleanupFilter();
    }
}
