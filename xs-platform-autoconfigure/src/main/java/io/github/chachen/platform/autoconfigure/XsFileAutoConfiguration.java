package io.github.chachen.platform.autoconfigure;

import io.github.chachen.platform.file.FileController;
import io.github.chachen.platform.file.FileProperties;
import io.github.chachen.platform.file.FileStorage;
import io.github.chachen.platform.file.LocalFileStorage;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(prefix = "xs.file", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(FileProperties.class)
@Import(FileController.class)
public class XsFileAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(FileStorage.class)
    @ConditionalOnProperty(prefix = "xs.file", name = "backend", havingValue = "local", matchIfMissing = true)
    FileStorage fileStorage(FileProperties p) {
        return new LocalFileStorage(p);
    }
}
