package io.github.chachen.platform.autoconfigure;
import io.github.chachen.platform.log.*;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import javax.sql.DataSource;
@AutoConfiguration(after=XsLogAutoConfiguration.class)
@ConditionalOnProperty(prefix="xs.log",name="enabled",havingValue="true",matchIfMissing=false)
@ConditionalOnBean(DataSource.class)
@EnableAsync
@Import(DatabaseLoginLogListener.class)
public class XsDatabaseLogAutoConfiguration {
    @Bean @ConditionalOnMissingBean PlatformOperationLogMapper platformOperationLogMapper(SqlSessionFactory factory) throws Exception {var mapper=new MapperFactoryBean<PlatformOperationLogMapper>(PlatformOperationLogMapper.class);mapper.setSqlSessionFactory(factory);mapper.afterPropertiesSet();return mapper.getObject();}
    @Bean @ConditionalOnMissingBean PlatformLoginLogMapper platformLoginLogMapper(SqlSessionFactory factory) throws Exception {var mapper=new MapperFactoryBean<PlatformLoginLogMapper>(PlatformLoginLogMapper.class);mapper.setSqlSessionFactory(factory);mapper.afterPropertiesSet();return mapper.getObject();}
    @Bean @ConditionalOnMissingBean(OperationLogWriter.class) OperationLogWriter databaseOperationLogWriter(PlatformOperationLogMapper mapper){return new DatabaseOperationLogWriter(mapper);}
}
