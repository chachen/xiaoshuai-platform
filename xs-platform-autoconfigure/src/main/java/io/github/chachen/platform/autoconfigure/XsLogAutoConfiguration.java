package io.github.chachen.platform.autoconfigure;

import io.github.chachen.platform.log.OperationLogAspect;
import io.github.chachen.platform.log.OperationLogWriter;
import io.github.chachen.platform.log.Slf4jOperationLogWriter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;

@AutoConfiguration
@ConditionalOnProperty(prefix = "xs.log", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(XsLogProperties.class)
public class XsLogAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean({OperationLogWriter.class, DataSource.class})
    OperationLogWriter slf4jOperationLogWriter() {
        return new Slf4jOperationLogWriter();
    }

    @Bean
    @ConditionalOnMissingBean
    OperationLogAspect operationLogAspect(OperationLogWriter writer) {
        return new OperationLogAspect(writer);
    }
}
