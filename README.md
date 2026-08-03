# Xiaoshuai Platform

面向个人 Web 项目的可复用 Spring Boot 基础平台。

## 当前状态

项目目前处于基础骨架阶段，已完成：

- Maven 多模块工程
- Spring Boot Starter 聚合模块
- 自动配置模块
- 本地 Maven 仓库安装
- 测试应用自动装配验证

以下能力尚未实现：

- 用户与角色管理
- 登录认证与权限控制
- 验证码
- 字典管理
- 操作日志与登录日志
- 文件存储
- 数据脱敏

## 模块说明

### xs-platform-core

存放与具体框架低耦合的公共接口、模型和基础能力。

### xs-platform-autoconfigure

存放 Spring Boot 自动配置类和配置属性。

### xs-platform-starter

提供给业务项目引用的聚合 Starter。

### xs-platform-test-app

用于验证 Starter 自动装配和后续功能集成。

## 当前使用方式

先安装到本地 Maven 仓库：

```bash
mvn clean install
