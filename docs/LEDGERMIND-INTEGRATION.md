# LedgerMind 接入与使用说明

本文面向需要接入 Xiaoshuai Platform 的业务项目，当前以 LedgerMind 为例。平台的目标是让业务项目通过 Maven 引入公共底座，业务代码只保留领域模型、业务接口和业务表。

## 1. 构建平台

在平台根目录执行：

~~~bash
mvn clean install
~~~

这会把平台各模块安装到本地 Maven 仓库。发布到团队私服时，可将同样的模块发布到 Nexus、私有 Maven Registry 等仓库，业务项目仍然只需要依赖 Starter。

## 2. 业务项目引入依赖

LedgerMind 的 POM 推荐只声明 Starter：

~~~xml
<dependency>
    <groupId>io.github.chachen.platform</groupId>
    <artifactId>xs-platform-starter</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
~~~

这是一个聚合 Starter。Maven 会自动解析 `xs-platform-core`、`xs-platform-web`、`xs-platform-auth` 等传递依赖，因此不是每个模块都要在 LedgerMind 的 POM 中重复声明，也不是生成一个把全部模块合并进去的 fat JAR。平台内部仍会产出多个普通 JAR，这是 Maven 组件化依赖的正常方式。

LedgerMind 当前已有自己的身份、JWT、MyBatis、组织权限和 API 契约，因此不建议现在直接引入完整 `xs-platform-starter`。当前更稳妥的路线是先复用低耦合模块或接口，例如验证码、`CaptchaStore`、脱敏注解和 `FileStorage`；平台的系统表、认证控制器和全局安全链暂不接管 LedgerMind。等双方的认证、响应和数据库契约完成适配后，再评估是否切换到 Starter。

## 3. 数据库依赖和配置

平台当前支持 MySQL 和 PostgreSQL。业务项目需要自己提供数据库驱动和连接信息；平台 Starter 不强制捆绑业务数据库驱动。

### PostgreSQL

LedgerMind 使用 PostgreSQL 时，业务项目引入：

~~~xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
~~~

~~~yaml
spring:
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/ledgermind
    username: postgres
    password: change-me
  flyway:
    enabled: true
~~~

平台不会自动修改宿主项目的 `spring.flyway.locations`。平台迁移位于独立路径 `classpath:db/xs-platform-migration/common`，不会被 LedgerMind 现有的 `classpath:db/migration` 扫描到；迁移版本从 `V9001` 起，也不会和业务常见的 `V1`～`V8` 冲突。当前平台核心迁移使用标准 SQL，因此 MySQL 和 PostgreSQL 共用 `common` 脚本，后续出现真正的方言差异时再放入 `db/xs-platform-migration/{vendor}`。

平台系统表不是每个接入项目都必须安装：只复用验证码或脱敏时不需要平台 SQL；启用 `xs-platform-system`、平台用户、角色、权限、菜单或数据库日志时才需要。

有两种安装方式：

1. 手工安装：执行项目中的 [`sql/xs-platform.sql`](../sql/xs-platform.sql)，适合 LedgerMind 这类已经有自己的 Flyway 历史和身份系统的项目。手工安装后不要把平台迁移路径加入 LedgerMind 的 Flyway locations。
2. Flyway 管理：在宿主项目已有迁移位置后追加平台位置：

~~~yaml
spring:
  flyway:
    locations:
      - classpath:db/migration
      - classpath:db/xs-platform-migration/common
      # 如果将来有数据库专属 V900x 脚本，再追加：
      # - classpath:db/xs-platform-migration/{vendor}
~~~

这样业务迁移和平台迁移由同一个 Flyway history table 管理，但平台使用 `V9001+` 版本避免冲突。若必须使用独立 history table，需要由宿主项目自行创建第二个 Flyway 实例；Starter 不会偷偷创建第二套 Flyway。

### MySQL

LedgerMind 使用 MySQL 时，业务项目引入：

~~~xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
~~~

~~~yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/ledgermind?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: change-me
  flyway:
    enabled: true
~~~

平台的 Flyway 脚本位于 `xs-platform-system/src/main/resources/db/xs-platform-migration`，当前包含：

- `common/V9001__create_platform_tables.sql`：用户、角色、权限、用户角色关系、角色权限关系和菜单
- `common/V9002__platform_logs_and_dict.sql`：操作日志、登录日志、字典类型和字典数据

平台表统一使用 `xs_` 前缀，例如 `xs_sys_user`、`xs_sys_role`、`xs_operation_log`，避免和业务项目或 LedgerMind 自己的 `sys_user` 等系统表冲突。单表主键由 MyBatis-Plus `ASSIGN_ID` 在应用层生成，不依赖 MySQL 自增或 PostgreSQL identity。

业务项目只维护自己的业务迁移脚本，不要修改平台已发布版本中的迁移脚本。生产环境建议先在独立数据库验证迁移，再执行部署。

## 4. 基础配置

LedgerMind 可以从下面的最小配置开始：

~~~yaml
xs:
  platform:
    application-name: ledger-mind
  database:
    enabled: true
  auth:
    enabled: true
    security-chain-enabled: false
    secret: replace-with-a-random-secret-at-least-32-bytes
    captcha-required: true
  system:
    enabled: true
    init-admin: true
    admin-password: replace-me-now
  captcha:
    enabled: true
  file:
    enabled: true
    root: /data/ledger-mind/files
  log:
    enabled: true
~~~

首次启动完成管理员初始化后，应将 `xs.system.init-admin` 改为 `false`，并使用密钥管理系统保存 `xs.auth.secret` 和数据库密码。不要在生产环境使用示例密码或示例 JWT Secret。

平台默认不注册自己的 Spring Security `SecurityFilterChain`。新项目需要平台认证时显式设置 `xs.auth.security-chain-enabled=true`；LedgerMind 这类已有自己的 JWT 解析和登录接口的项目保持默认值即可。若要完全不启用平台认证模块，则设置 `xs.auth.enabled=false`。

平台默认也不注册全局异常处理器。新项目可设置 `xs.web.global-error-handler-enabled=true`；已有 `@ControllerAdvice` 的业务项目保持默认值，并通过 `ApiErrorCodeMapper` 或自己的异常处理器维护业务错误码契约。

### 统一响应

平台 Web 模块当前返回与 LedgerMind 兼容的 envelope：

~~~json
{
  "code": 0,
  "message": "success",
  "data": {},
  "requestId": "..."
}
~~~

请求标识优先使用 `X-Request-Id`，同时保留 `X-Trace-Id` 兼容旧调用。异常响应也会携带 `requestId`，便于从日志定位请求。

如暂时不使用某项能力，可以关闭对应开关，例如 `xs.captcha.enabled=false`、`xs.auth.enabled=false`、`xs.dict.enabled=false`、`xs.file.enabled=false` 或 `xs.log.enabled=false`。

## 5. Redis 可选扩展

Redis 不是 Starter 的强制依赖。需要验证码和字典使用 Redis 时，业务项目额外引入：

~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
~~~

~~~yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
~~~

检测到 `StringRedisTemplate` 后，平台会自动使用 Redis 验证码存储和 Redis 字典服务；业务项目也可以自己提供 `CaptchaStore` 或 `DictService` Bean 覆盖默认实现。

当前登录失败锁定仍是单实例内存实现。多实例部署所需的 Redis 分布式限流、登录锁定和 Token 撤销，列在项目 README 待办中。

## 6. MinIO 可选扩展

需要 MinIO 时，业务项目额外引入：

~~~xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.17</version>
</dependency>
~~~

~~~yaml
xs:
  file:
    backend: minio
    endpoint: http://127.0.0.1:9000
    access-key: minioadmin
    secret-key: minioadmin
    bucket: ledgermind
~~~

默认文件后端是本地文件系统。使用 MinIO 时，平台会自动切换为 MinIO 存储；生产环境应替换示例账号密码，并提前创建或授权目标 bucket。

## 7. 认证和权限使用流程

平台默认白名单包括登录、刷新、验证码和错误页，其他业务接口默认需要认证。

典型前端调用顺序：

1. `GET /api/captcha` 获取验证码图片和验证码标识。
2. `POST /api/auth/login` 提交用户名、密码、验证码标识和验证码文本。
3. 保存登录返回的 access token。
4. 后续请求携带 `Authorization: Bearer <token>`。
5. Token 临近过期时调用 `POST /api/auth/refresh`；退出时调用 `POST /api/auth/logout`。
6. 页面初始化可调用 `GET /api/auth/me` 获取当前用户。

业务接口可以使用接口级权限注解：

~~~java
@RequirePermission("ledgermind:report:read")
@GetMapping("/reports")
public ApiResult<?> reports() {
    return ApiResult.success(reportService.list());
}
~~~

用户、角色、权限和菜单的基础接口如下：

- `GET/POST /api/system/users`
- `GET/POST /api/system/roles`
- `GET/POST /api/system/permissions`
- `GET/POST /api/system/menus`

权限字符串由业务项目自行约定，建议采用 `业务域:资源:动作` 格式，例如 `ledgermind:report:read`、`ledgermind:report:export`。

## 8. 数据权限说明

当前平台已经能判断“用户是否有权调用接口”，但还没有自动判断“用户能看到哪些数据”。后者就是数据权限。

例如：

- 接口权限：用户拥有 `ledgermind:report:read`，因此可以调用报表查询接口。
- 数据权限：普通用户只能查询 `owner_id = 当前用户 id` 的报表；部门负责人可以查询本部门；管理员可以查询全部。

数据权限通常依赖部门/组织树、用户与部门关系、数据归属字段，以及 Service、Mapper 或租户条件的统一过滤。它不能仅靠给接口加一个权限字符串完成。LedgerMind 当前先不启用数据权限，后续有跨部门或多租户需求时再单独设计，避免过早把业务规则耦合进平台。

## 9. 平台接口速查

- 验证码：`GET /api/captcha`、`POST /api/captcha/verify`
- 认证：`POST /api/auth/login`、`POST /api/auth/refresh`、`POST /api/auth/logout`、`GET /api/auth/me`
- 系统管理：`/api/system/users`、`/api/system/roles`、`/api/system/permissions`、`/api/system/menus`
- 字典：`GET /api/dict/{type}`
- 文件：`POST /api/files`、`GET /api/files/{key}`

统一响应、异常码和校验错误格式以平台 `ApiResult` 及 Web 模块实现为准；业务项目不要重复实现一套相互冲突的全局异常处理。

## 10. 当前不包含的能力

当前版本刻意不实现以下能力，后续根据真实业务需要增加：

- 组织架构、部门数据权限、归属人/项目/租户范围过滤
- Redis 分布式限流、分布式登录失败锁定、Token 撤销和黑名单
- 短信/邮箱验证码和第三方消息通道
- 在线用户、会话管理和强制下线
- S3 兼容对象存储临时签名 URL、文件元数据和分片上传
- SQL Server、Oracle 等其他数据库 SQL 方言和迁移脚本

这些事项已同步记录在项目根目录 README 的“待办”中。
