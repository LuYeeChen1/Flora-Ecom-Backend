# Implementation Map

本文件总结 **当前仓库已实现的功能**，并按“功能 ➜ 文件组合”给出定位索引。

## 1) 健康检查（/health）

**功能**：匿名可访问的健康检查接口，返回 `{ status: "ok" }`。

**文件组合**：
- `interfaces/controller/health/HealthController` 负责 HTTP 路由与响应构造。
- `infrastructure/security/SecurityConfig` 放行 `/health` 匿名访问。

**关键路径**：
- Controller：`src/main/java/com/backend/flowershop/interfaces/controller/health/HealthController.java`
- Security：`src/main/java/com/backend/flowershop/infrastructure/security/SecurityConfig.java`

---

## 2) 当前用户信息（/me）

**功能**：返回当前 JWT 用户信息（sub/email/groups/roleStage）。

**文件组合**：
- `MeController` 接收请求并提取 principal。
- `AuthPrincipalExtractor` 从 `JwtAuthenticationToken` 中解析 claims。
- `GetMeService` 返回 principal（最小实现）。
- `MeMapper` 将 principal 转为 `MeResponse` DTO。

**关键路径**：
- Controller：`src/main/java/com/backend/flowershop/interfaces/controller/me/MeController.java`
- DTO/Mapper：`src/main/java/com/backend/flowershop/interfaces/controller/me/dto/MeResponse.java`、`src/main/java/com/backend/flowershop/interfaces/controller/me/mapper/MeMapper.java`
- UseCase/Service：`src/main/java/com/backend/flowershop/application/port/in/me/GetMeUseCase.java`、`src/main/java/com/backend/flowershop/application/service/me/GetMeService.java`
- Security：`src/main/java/com/backend/flowershop/interfaces/security/AuthPrincipal.java`、`src/main/java/com/backend/flowershop/interfaces/security/AuthPrincipalExtractor.java`

---

## 3) Seller Onboarding 提交（/seller/onboarding/submit）

**功能**：提交/更新 Seller Onboarding 数据，校验并保存到数据库。

**文件组合**：
- Controller：接收 DTO，提取 principal。
- Mapper：将请求 DTO 转为 Domain 模型。
- UseCase/Service：调用 Validator 并持久化。
- Validator：标准化 + 规则流水线。
- Rule Pipeline：编排 Auth/RoleStage/Profile/Document 规则。
- RuleImpl：具体字段校验。
- Repository：JDBC 持久化到 `seller_onboarding` / `seller_document`。
- ClockPort：提供时间戳。

**关键路径**：
- Controller：`src/main/java/com/backend/flowershop/interfaces/controller/seller/SellerOnboardingController.java`
- DTO/Mapper：`src/main/java/com/backend/flowershop/interfaces/controller/seller/dto/SubmitSellerOnboardingRequest.java`、`src/main/java/com/backend/flowershop/interfaces/controller/seller/mapper/SellerOnboardingMapper.java`
- UseCase/Service：`src/main/java/com/backend/flowershop/application/port/in/seller/SubmitSellerOnboardingUseCase.java`、`src/main/java/com/backend/flowershop/application/service/seller/SubmitSellerOnboardingService.java`
- Validator：`src/main/java/com/backend/flowershop/application/validator/SellerOnboardingValidator.java`
- Pipeline：`src/main/java/com/backend/flowershop/application/pipeline/DefaultRulePipeline.java`
- Rules：`src/main/java/com/backend/flowershop/application/rules/**`
- RuleImpl：`src/main/java/com/backend/flowershop/application/ruleimpl/**`
- Persistence：`src/main/java/com/backend/flowershop/infrastructure/persistence/seller/SellerOnboardingJdbcRepository.java`
- Clock：`src/main/java/com/backend/flowershop/application/port/out/clock/ClockPort.java`、`src/main/java/com/backend/flowershop/infrastructure/clock/SystemClockAdapter.java`

---

## 4) Seller Onboarding 查询明细（/seller/onboarding/me）

**功能**：获取当前用户的 Seller Onboarding 明细。

**文件组合**：
- Controller 调用 UseCase。
- Service 读取 Repository。
- Mapper 将 Domain 输出为 Response DTO。

**关键路径**：
- Controller：`src/main/java/com/backend/flowershop/interfaces/controller/seller/SellerOnboardingController.java`
- Service：`src/main/java/com/backend/flowershop/application/service/seller/GetMySellerOnboardingService.java`
- Repository：`src/main/java/com/backend/flowershop/application/port/out/seller/SellerOnboardingRepository.java`、`src/main/java/com/backend/flowershop/infrastructure/persistence/seller/SellerOnboardingJdbcRepository.java`
- Mapper/DTO：`src/main/java/com/backend/flowershop/interfaces/controller/seller/mapper/SellerOnboardingMapper.java`、`src/main/java/com/backend/flowershop/interfaces/controller/seller/dto/SellerOnboardingResponse.java`

---

## 5) Seller Onboarding 状态查询（/seller/onboarding/status）

**功能**：返回数据库状态与 JWT role_stage 对照。

**文件组合**：
- Controller 调用 Status UseCase。
- Service 读取 Repository + 使用 principal.roleStage。
- DTO 统一输出结构。

**关键路径**：
- Controller：`src/main/java/com/backend/flowershop/interfaces/controller/seller/SellerOnboardingController.java`
- Service：`src/main/java/com/backend/flowershop/application/service/seller/GetMySellerOnboardingStatusService.java`
- Repository：`src/main/java/com/backend/flowershop/infrastructure/persistence/seller/SellerOnboardingJdbcRepository.java`
- DTO：`src/main/java/com/backend/flowershop/interfaces/controller/seller/dto/SellerOnboardingStatusResponse.java`

---

## 6) 统一校验体系（Rule + Validator + Normalize）

**功能**：标准化输入并运行规则，输出结构化错误。

**文件组合**：
- Normalizer：文本、电话、SellerProfile 标准化。
- Pipeline：按顺序执行规则并汇总错误。
- RuleImpl：具体字段校验（Auth/RoleStage/Profile/Document）。
- ValidationResult/Exception：输出结构化错误，交由全局异常处理。

**关键路径**：
- Normalizer：`src/main/java/com/backend/flowershop/application/normalize/**`
- Pipeline：`src/main/java/com/backend/flowershop/application/pipeline/**`
- Rules/RuleImpl：`src/main/java/com/backend/flowershop/application/rules/**`、`src/main/java/com/backend/flowershop/application/ruleimpl/**`
- Validation：`src/main/java/com/backend/flowershop/application/validator/**`

---

## 7) 统一错误输出

**功能**：将校验失败、业务异常、鉴权异常映射为统一 JSON 错误。

**文件组合**：
- `ValidationFailedException` 在校验失败时抛出。
- `GlobalExceptionHandler` 将异常映射为 `ApiErrorResponse`。

**关键路径**：
- `src/main/java/com/backend/flowershop/application/validator/ValidationFailedException.java`
- `src/main/java/com/backend/flowershop/interfaces/controller/common/GlobalExceptionHandler.java`
- `src/main/java/com/backend/flowershop/interfaces/controller/common/ApiErrorResponse.java`

---

## 8) 安全与认证（Cognito JWT Resource Server）

**功能**：JWT 校验、audience 校验、groups -> ROLE 映射。

**文件组合**：
- `SecurityConfig` 定义路由访问策略与 JWT converter。
- `JwtDecoderConfig` 使用 issuer/jwk 配置并添加 audience 校验。
- `AudienceValidator` 实现 aud 校验。
- `JwtGroupsConverter` 将 Cognito groups 转为角色。

**关键路径**：
- `src/main/java/com/backend/flowershop/infrastructure/security/SecurityConfig.java`
- `src/main/java/com/backend/flowershop/infrastructure/security/JwtDecoderConfig.java`
- `src/main/java/com/backend/flowershop/infrastructure/security/AudienceValidator.java`
- `src/main/java/com/backend/flowershop/infrastructure/security/JwtGroupsConverter.java`

---

## 9) 持久化（JDBC + MySQL）

**功能**：通过 JDBC 访问 `seller_onboarding` 与 `seller_document` 表。

**文件组合**：
- `JdbcConfig` 提供 JdbcTemplate。
- `SellerOnboardingJdbcRepository` 实现保存与查询。
- `SellerOnboardingRowMapper` 映射主表记录。

**关键路径**：
- `src/main/java/com/backend/flowershop/infrastructure/persistence/jdbc/JdbcConfig.java`
- `src/main/java/com/backend/flowershop/infrastructure/persistence/seller/SellerOnboardingJdbcRepository.java`
- `src/main/java/com/backend/flowershop/infrastructure/persistence/seller/mapper/SellerOnboardingRowMapper.java`

---

## 10) 领域模型

**功能**：承载 Seller Onboarding 领域结构与错误模型。

**关键路径**：
- `src/main/java/com/backend/flowershop/domain/model/**`
- `src/main/java/com/backend/flowershop/domain/error/**`
