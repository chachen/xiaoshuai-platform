package io.github.chachen.platform.autoconfigure;

import io.github.chachen.platform.dict.DictController;
import io.github.chachen.platform.dict.DictService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(prefix = "xs.dict", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(XsDictProperties.class)
@Import(DictController.class)
public class XsDictAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(DictService.class)
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass("org.springframework.data.redis.core.StringRedisTemplate")
    DictService dictService() {
        return new DictService();
    }
}
