# Xiaoshuai Platform

面向个人 Web 项目的可复用 Spring Boot 基础平台。目标是把验证码、认证、用户和角色、接口权限、数据库迁移、文件、字典、日志等公共能力沉淀为可插拔的 Maven Starter，后续业务项目只需要引入依赖并配置即可使用。

## 当前已具备

- Maven 多模块工程和 `xs-platform-starter` 聚合 Starter
- Spring Boot 自动配置；默认能力可按配置关闭
- TraceId/MDC、统一响应、统一异常和参数校验
- 图片验证码：TTL、一次性消费、大小写配置；有 Redis 时可切换 Redis 存储
- MyBatis-Plus 分页、防全表更新删除、字段填充和 Flyway 迁移
- PostgreSQL 和 MySQL 兼容迁移脚本；平台表使用 `xs_` 前缀，主键由应用层生成
- 用户、角色、权限、菜单基础管理，BCrypt 密码和管理员初始化开关
- JWT 登录、刷新、注销、当前用户、白名单和登录失败锁定
- `@RequirePermission` 接口级权限校验
- 数字 `code=0`、`requestId` 统一响应；可关闭平台全局 SecurityFilterChain 以复用业务项目现有安全链
- 系统管理接口默认使用 `@RequirePermission` 做细粒度权限保护
- 字典本地缓存；有 Redis 时可切换 Redis
- 本地文件存储；可选 MinIO 后端
- 操作日志切面、数据库异步操作日志、登录日志和 Jackson 脱敏
- H2 测试应用，用于验证 Starter 自动装配和 Flyway 启动流程

## 模块与依赖方式

业务项目推荐只引用一个依赖：

~~~xml
<dependency>
    <groupId>io.github.chachen.platform</groupId>
    <artifactId>xs-platform-starter</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
~~~

`xs-platform-starter` 是聚合 Starter，不是把所有 class 打进一个 uber/fat JAR。平台内部各模块仍然分别构建 JAR，由 Maven 通过传递依赖自动解析；业务项目的 POM 通常只需要声明 Starter。需要 Redis、MinIO 或数据库驱动时，再由业务项目按需增加对应依赖。

模块职责如下：

- `xs-platform-core`：公共接口、模型和低耦合基础能力
- `xs-platform-web`：Web 层统一响应、异常、校验、TraceId
- `xs-platform-captcha`：验证码
- `xs-platform-database`：MyBatis-Plus、分页、Flyway
- `xs-platform-system`：用户、角色、权限、菜单、字典和系统迁移脚本
- `xs-platform-auth`：JWT 认证和接口权限
- `xs-platform-file`：文件存储抽象、本地存储和 MinIO
- `xs-platform-log`：操作日志、登录日志和脱敏
- `xs-platform-autoconfigure`：自动配置
- `xs-platform-starter`：业务项目入口依赖

## 使用文档

完整接入说明、依赖、配置、数据库迁移、接口调用顺序、Redis/MinIO 扩展和 LedgerMind 示例见：

[LedgerMind 接入与使用说明](docs/LEDGERMIND-INTEGRATION.md)

注意：LedgerMind 当前已有自己的 PostgreSQL、身份认证、JWT、安全链和 API 响应契约，现阶段不直接把完整 Starter 接入生产；先复用低耦合能力，待契约适配完成后再评估整体接入。平台不会自动改写宿主项目的 `spring.flyway.locations`，也不会默认注册全局安全链和全局异常处理器。

本地构建并安装到 Maven 仓库：

~~~bash
mvn clean install
~~~

## 当前权限边界

当前实现的是接口级权限：例如用户是否拥有 `ledgermind:report:read`，决定其能否调用某个接口。

数据权限是接口通过之后，对查询结果继续做数据范围过滤。例如同一个“查看报表”接口，用户 A 只能看到自己创建的数据，部门负责人能看到本部门数据，管理员能看到全部数据。它通常需要组织架构、部门关系、数据归属字段以及查询条件改写，和接口级权限不是一回事。

LedgerMind 当前先使用接口级权限即可，数据权限暂不实现，已列入待办。

## 待办

以下能力目前不影响 LedgerMind 的基础接入，按实际业务需要再实施：

- [ ] 数据权限：部门/组织、数据归属人、项目或租户范围过滤
- [ ] Redis 分布式限流、分布式登录失败锁定、Token 撤销和黑名单
- [ ] 短信验证码、邮箱验证码及第三方消息通道抽象
- [ ] 在线用户、会话管理和强制下线
- [ ] S3 兼容对象存储、临时签名 URL、文件元数据和分片上传
- [ ] SQL Server、Oracle 等其他数据库方言与迁移脚本适配
- [ ] 更完整的单元测试、集成测试和安全回归测试

## 设计原则

平台对外暴露 `AccountProvider`、`CaptchaStore`、`FileStorage` 等接口，默认实现只作为可替换兜底。系统数据库实体不会直接作为业务 API 返回对象；认证模块只依赖 AccountProvider 投影。参考 RuoYi-Vue-Plus 的成熟表模型和安全实践时，已重新组织为本项目包名和模块边界，没有把若依作为 Maven 依赖。

## 参考与声明

本项目部分设计参考 RuoYi-Vue-Plus，第三方组件和参考代码说明见 [THIRD-PARTY-NOTICES.md](THIRD-PARTY-NOTICES.md)。
