# FlowerShop Backend

**Repository Entry · Documentation Orchestrator**

本仓库是 FlowerShop 后端项目的**唯一源码与文档仓库**，
同时也是 **AI / Codex 协作的唯一上下文入口**。

README **不承载架构细节**，
它的唯一职责是：**编排「应该先读哪些 `.md` 文件，以及哪些不能乱动」**。

---

## 🎯 本仓库解决什么问题

FlowerShop 是一个以 **0 成本 / Free Tier** 为硬约束的电商后端项目，
采用以下技术组合：

* AWS Cognito（认证 / 授权）
* Serverless（Lambda + API Gateway）
* Spring Boot（Clean Architecture）
* 本地 MySQL（开发 / 演示）

---

## ⚠️ 强制规则（AI / Codex 必须遵守）

* ❌ 禁止假设任何「未在文档中明确声明为已完成 / 已冻结」的能力已经存在
* ❌ 禁止跳过 README 直接生成代码
* ❌ 禁止只看单一文档（例如只看 ARCHITECTURE.md）

> **所有行为必须以本文档定义的阅读顺序为准。**

---

## 📚 文档阅读顺序（强制）

### Step 0：仓库入口（你正在读）

* **README.md（本文件）**

职责：

* 决定「读什么 / 不读什么 / 先后顺序」
* 标注哪些文档是 **权威冻结**
* 标注哪些文档是 **过程 / 参考**

---

### Step 1：全局架构与边界（最高优先级 · 冻结）

* **`docs/ARCHITECTURE.md`**

这是 **全仓库唯一的架构权威文档**，定义了：

* Clean Architecture 分层
* 依赖方向
* Rule / Validation 体系
* 技术与代码级硬约束

📌 **任何代码、设计、审计行为都不得违反此文件**。

> ❗ 未完整阅读 `ARCHITECTURE.md` 即开始输出代码，视为严重违规。

---

### Step 2：项目整体定位（冻结）

本节整合了 `PROJECT_OVERVIEW` 的内容，说明项目定位与边界。

* 开发 / 演示 / FYP
* 0 成本（Free Tier）
* 前后端完全分离（REST API）

#### 核心原则

* 云端能力优先（Cognito / API Gateway / Lambda / S3）
* Lambda 只做“云端权限动作”，不做业务
* 业务数据存本地 MySQL
* Seller / Admin 等状态 **只由 Cognito Group 表达**

#### 当前阶段

* Seller Onboarding 尚未冻结
* 接下来由 Codex 完成 Frontend + Backend 代码
* 本文档集是 Cloud / 权限 / 流程的唯一事实来源

---

### Step 3：当前进度锚点（动态）

本节整合了 `CURRENT_PROGRESS` 的内容，用于告诉你「现在做到哪」。

#### 已完成

* Cognito Groups 全部创建
* ADMIN 账号已手动加入 ADMIN group
* API Gateway + JWT Authorizer 已配置
* Lambda：
  * move-to-seller-pending
  * approve-seller（含 Guard）
  * reject-seller（含 Guard）
* S3 Bucket 已创建（私有 + CORS）

#### 未冻结

* Seller Onboarding（等待 Frontend + Backend 完成并端到端测试）

#### 下一步（由 Codex 完成）

* Frontend Seller Submit (这个由frontend 的 repository 来做，backend的 repository只需专注 backend)
* Backend Seller Onboarding Service
* Admin Review UI + Backend

---

### Step 4：Cloud / Auth / Serverless（按需阅读）

当任务涉及 **云端、认证或 Serverless** 时，阅读以下内容：

#### Cloud Architecture

**使用的 AWS 服务（全部在 Free Tier 内）**

* Amazon Cognito（认证 + Group）
* API Gateway HTTP API（REST API）
* AWS Lambda（只做 Cognito Group 迁移）
* Amazon S3（Seller 材料上传，可选）
* CloudWatch Logs（默认，用于排错）

**不使用的服务（刻意不用）**

* 不使用 DynamoDB / RDS
* 不使用 Step Functions
* 不使用 SES / SNS
* 不使用 OCR / AI / 风控类服务

**总体结构（逻辑）**

Frontend
→ API Gateway (JWT Authorizer)
→ Lambda（改 Cognito Group）
→ Backend（Spring Boot / MySQL）

说明：

* Frontend 只通过 API Gateway 访问 Lambda
* Backend 不直接操作 Cognito Group
* Lambda 永远不访问 MySQL

#### Cognito & Authentication

**Cognito 基本设置**

* 单一 User Pool
* 单一 App Client
* Public Client + PKCE
* Hosted UI 登录
* 使用 access_token 作为后端鉴权 token

**Cognito Groups（冻结）**

业务组（同一时间只能存在一个）

* CUSTOMER
* SELLER_PENDING
* SELLER
* SELLER_REJECTED

权限组（可叠加）

* ADMIN

规则：

* 业务组必须单选
* ADMIN 可与任一业务组共存
* 不使用 Group Precedence
* 不使用 custom attributes / role_stage

**Token 使用约定**

* Frontend：
  * 调 API 时统一使用 `Authorization: Bearer <access_token>`
* Backend：
  * 用 access_token 校验身份
  * 如需要 email，可通过 Cognito UserInfo 获取并缓存

#### API Gateway & Lambda

**API Gateway**

* 类型：HTTP API
* 鉴权：JWT Authorizer（Cognito）
* 所有受保护接口都必须走 Authorizer

**Lambda 设计原则**

* 只做 Cognito Group 迁移
* 不读 / 不写 MySQL
* 不做业务校验
* 不处理文件
* 不生成 token

**已存在的 Lambda API**

1. `POST /groups/move-to-seller-pending`
   * 当前登录用户自己
   * CUSTOMER → SELLER_PENDING
2. `POST /groups/approve-seller`
   * ADMIN 审核别人
   * SELLER_PENDING → SELLER
3. `POST /groups/reject-seller`
   * ADMIN 审核别人
   * SELLER_PENDING → SELLER_REJECTED

**Lambda Guard（已实现）**

* 仅 ADMIN 可 approve / reject
* ADMIN 不可审核自己
* 只能审核 SELLER_PENDING
* 业务组只能存在一个（状态机自检）

**Lambda IAM 权限（最小）**

* cognito-idp:AdminAddUserToGroup
* cognito-idp:AdminRemoveUserFromGroup
* cognito-idp:AdminListGroupsForUser
* Resource 锁定到当前 User Pool ARN

#### S3 Design (Optional)

**用途**

* Seller 提供证明材料（图片 / PDF 等）

**设计模式**

* S3 + Pre-signed URL
* 前端直传 S3
* Backend 只负责：
  * 发 presign URL
  * 记录 object key 到 MySQL

**已完成**

* 私有 S3 Bucket 已创建
* Block Public Access 全开启
* Bucket Owner Enforced
* 最小 CORS（localhost）

**尚未完成**

* IAM Policy（发 presign 用）
* presign 接口设计（只设计，不写 Lambda）

**明确不做**

* 不做 OCR
* 不做自动审核
* 不做 S3 事件触发

#### Seller Onboarding Flow

**总体模式**

* 系统自动初筛（Backend）
* ADMIN 人工终审（Frontend + Lambda）

**Seller 提交流程**

1. Seller 填写业务资料（不包含 Cognito 已有字段）
2. Backend 校验并写 MySQL：
   * status = PENDING_REVIEW
3. Backend 调用：
   * POST /groups/move-to-seller-pending
4. Cognito：
   * CUSTOMER → SELLER_PENDING

**Admin 审核流程**

1. Admin 登录（必须有 ADMIN group）
2. Backend 查询 MySQL pending 列表
3. Admin 点 approve / reject
4. Backend：
   * 更新 MySQL 状态 + reason
   * 调用对应 Lambda
5. Cognito Group 迁移：
   * approve → SELLER
   * reject  → SELLER_REJECTED

**关键约束**

* Lambda 不知道 MySQL 的存在
* Backend 不直接改 Cognito Group

---

### Step 5：Backend 功能与约定（参考）

本节整合了当前后端已实现功能与执行步骤。

#### 1. 应用启动与基础结构

**功能说明**：启动 Spring Boot 应用，装配各层组件（接口层、应用层、基础设施层、领域层）。

**执行步骤**：

1. 通过 `SpringApplication.run` 启动应用。
2. Spring Boot 自动扫描并装配 `@Service`、`@Repository`、`@Component`、`@Configuration` 等组件。

**涉及文件**：

* `src/main/java/com/backend/flowershop/FlowerShopApplication.java`

#### 2. 认证与资源服务器（Cognito JWT）

**功能说明**：

* 作为 JWT Resource Server 验证 Cognito access token。
* 校验 token 使用场景（token_use=access）和 client_id。
* 仅允许 `/me` 通过认证访问，其余接口拒绝。
* 支持 CORS 配置。

**执行步骤**：

1. 读取配置（issuer、client_id、jwk-set-uri、CORS 白名单）。
2. 构建 `JwtDecoder` 并加载两类验证器：Issuer 校验 + access token/client_id 校验。
3. SecurityFilterChain 配置：
   * 允许 `OPTIONS` 预检。
   * `/me` 需要登录。
   * 其他接口拒绝。
4. 通过 CORS 配置允许指定来源与方法。

**涉及文件**：

* `src/main/java/com/backend/flowershop/infrastructure/security/SecurityConfig.java`
* `src/main/java/com/backend/flowershop/infrastructure/security/AccessTokenValidator.java`
* `src/main/resources/application-dev.properties`

#### 3. 当前用户资料查询（/me）

**功能说明**：

* 提供 `GET /me` 返回当前用户的基本信息（subject、username、email、groups）。
* 从 JWT 中解析用户信息，并补充缺失邮箱。

**执行步骤**：

1. Controller 接收 `GET /me`，从 JWT 中解析 subject、username、email、groups、access token。
2. 调用应用层用例 `CurrentUserProfileUseCase.fetch` 获取用户资料。
3. 应用层检查本地用户表：
   * 若已有 email，直接返回。
   * 若没有 email，尝试从 Cognito UserInfo 拉取并缓存到本地数据库。
4. 返回 `UserProfileResponse` 给前端。

**涉及文件**：

* `src/main/java/com/backend/flowershop/interfaces/controller/MeController.java`
* `src/main/java/com/backend/flowershop/application/service/CurrentUserProfileService.java`
* `src/main/java/com/backend/flowershop/application/port/usecase/CurrentUserProfileUseCase.java`
* `src/main/java/com/backend/flowershop/interfaces/controller/dto/UserProfileResponse.java`
* `src/main/java/com/backend/flowershop/domain/model/TokenClaims.java`
* `src/main/java/com/backend/flowershop/domain/model/UserProfile.java`

#### 4. UserInfo 邮箱补全与缓存

**功能说明**：

* 当 JWT 中不含 email 时，调用 Cognito UserInfo API 获取 email。
* 将 email 持久化到本地 MySQL 表（`users`）。

**执行步骤**：

1. `CurrentUserProfileService` 尝试从用户表读取 email。
2. 若未命中：
   * 使用 access token 调用 UserInfo API。
   * 校验 email 非空后写入本地 `users` 表。
3. 返回补全后的 email。

**涉及文件**：

* `src/main/java/com/backend/flowershop/application/service/CurrentUserProfileService.java`
* `src/main/java/com/backend/flowershop/infrastructure/security/CognitoUserInfoClient.java`
* `src/main/java/com/backend/flowershop/application/port/security/UserInfoEmailPort.java`
* `src/main/java/com/backend/flowershop/infrastructure/persistence/JdbcUserAccountRepository.java`
* `src/main/java/com/backend/flowershop/domain/model/UserAccount.java`
* `src/main/resources/application.properties`
* `src/main/resources/application-dev.properties`

#### 5. 统一领域错误输出

**功能说明**：

* 捕获领域异常并统一返回错误结构。

**执行步骤**：

1. 领域层抛出 `DomainErrorException`。
2. `DomainErrorHandler` 捕获异常并返回 `ErrorResponse`。

**涉及文件**：

* `src/main/java/com/backend/flowershop/domain/error/DomainErrorException.java`
* `src/main/java/com/backend/flowershop/interfaces/controller/DomainErrorHandler.java`
* `src/main/java/com/backend/flowershop/interfaces/controller/dto/ErrorResponse.java`

#### 6. 本地数据库存储（用户表）

**功能说明**：

* 使用 JDBC 访问本地 MySQL `users` 表。
* 支持根据 subject 查询与 upsert 邮箱。

**执行步骤**：

1. `JdbcUserAccountRepository` 使用 SQL 查找 `sub`。
2. 使用 `INSERT ... ON DUPLICATE KEY UPDATE` 进行 upsert。

**涉及文件**：

* `src/main/java/com/backend/flowershop/infrastructure/persistence/JdbcUserAccountRepository.java`
* `src/main/resources/application-dev.properties`

---

### Step 6：Frontend（仅在涉及前端时）

#### Frontend Overview

**一句话总结**

这是一个基于 **Vue 3 + Vite + Tailwind CSS** 的单页应用（SPA），  
通过 **Amazon Cognito OAuth2 Authorization Code + PKCE** 完成登录认证，  
登录后提供受保护的 `/app/*` 区域，并通过 `authFetch()` 统一完成登录态 API 请求。

**核心能力（Key Points）**

* OAuth2 + PKCE（S256）
* Cognito Hosted UI（不自建登录页）
* sessionStorage 管理 OAuth 临时数据 / token / userInfo
* Vue Router 明确区分登录前 / 登录后
* `authFetch()` 统一处理 token、刷新与 401 重试
* Tailwind CSS 提供一致 UI

**前端职责边界**

前端负责：

* 登录 / 登出跳转
* token 获取、存储、刷新
* 路由区分与 UI 控制
* API 请求自动携带 token

前端不负责：

* 用户权限最终判断
* 角色/权限管理逻辑
* 数据合法性校验（由后端负责）

**当前实现进度（截至目前）**

* ✅ 认证流程：Cognito OAuth2 + PKCE 登录/回调/登出全链路已实现
* ✅ token 生命周期：获取、刷新、401 重试、登出清理已实现
* ✅ 路由结构：登录/回调/登出/登录后区域路由已完成
* ✅ 用户展示：userInfo 拉取 + JWT 兜底展示已实现
* ✅ 示例页面：`/app/me` 调用受保护接口并展示结果
* ✅ 文档映射：功能 → 文件映射与验证清单已维护

#### Frontend Feature File Mapping

（功能与文件组合对应关系｜最终严格一致版）

> 约束说明  
> - 本文档 **对齐当前代码**  
> - **只列出真实存在且被使用的文件**  
> - 每一个文件 **至少在一个功能点中出现一次**

##### 一、应用启动与路由骨架

**功能**  
初始化 Vue 应用、挂载 Router、启用 Tailwind 样式；  
通过路由划分登录区、回调页、登出页与登录后受保护区域。

**文件组合**

* `index.html`  
  * SPA 挂载入口
* `src/main.js`  
  * 创建 Vue App  
  * 挂载 Router  
  * 引入全局样式
* `src/style.css`  
  * Tailwind CSS 全局样式入口
* `src/App.vue`  
  * 顶层组件，仅渲染 `<router-view />`
* `src/router/index.js`  
  * 定义路由结构：`/`、`/callback`、`/signed-out`、`/app/*`
* `vite.config.js`  
  * Vite 构建配置
* `package.json`  
  * 项目依赖与脚本（dev / build / preview）

##### 二、Cognito 登录入口（OAuth2 + PKCE）

**功能**  
未登录用户点击登录按钮后：  
生成 PKCE verifier / challenge 与 state，  
保存到 sessionStorage，  
跳转到 Cognito Hosted UI。

**文件组合**

* `src/pages/Auth.vue`  
  * 登录入口页面 UI  
  * 触发登录流程
* `src/auth/login/oauth.ts`  
  * 生成 state  
  * 调用 PKCE 工具  
  * 拼接 Cognito authorize URL  
  * 执行浏览器跳转
* `src/auth/login/pkce.ts`  
  * 生成 PKCE verifier  
  * 生成 PKCE challenge（SHA-256 + base64url）
* `src/auth/storage/tempStorage.ts`  
  * 保存 OAuth 临时数据（state / verifier）
* `src/auth/config/cognito.ts`  
  * Cognito OAuth 配置  
  * domain / client_id / redirect_uri（来自 env）

##### 三、OAuth 回调处理（换 token + 拉 userInfo + 跳转）

**功能**  
Cognito 登录完成后回调：  
校验 state → 换 token → 缓存 token → 拉取 userInfo → 跳转到 `/app/me`。

**文件组合**

* `src/pages/Callback.vue`  
  * 回调页面 UI  
  * 显示加载 / 错误状态  
  * 触发回调处理逻辑
* `src/auth/callback/handleCallback.ts`  
  * 校验 state  
  * 使用授权码换 token  
  * 保存 token  
  * 协调拉取 userInfo
* `src/auth/callback/tokenExchange.ts`  
  * authorization code → token
* `src/auth/request/tokenTypes.ts`  
  * token 响应结构类型定义
* `src/auth/storage/tokenStorage.ts`  
  * 保存 access_token / refresh_token / 获取时间
* `src/auth/storage/userInfoStorage.ts`  
  * 缓存 userInfo
* `src/auth/userInfo.ts`  
  * 请求 Cognito `/oauth2/userInfo`
* `src/auth/config/cognito.ts`  
  * token / userInfo 端点配置

##### 四、登录态 API 请求（authFetch）

**功能**  
统一封装登录态 API 请求：  
自动携带 Bearer token、  
在过期前刷新、  
401 时自动重试一次。

**文件组合**

* `src/auth/request/authFetch.ts`  
  * 核心封装逻辑  
  * token 判断  
  * refresh 调度  
  * 401 重试
* `src/auth/request/refreshToken.ts`  
  * refresh_token → 新 token
* `src/auth/request/tokenTypes.ts`  
  * token 响应类型
* `src/auth/storage/tokenStorage.ts`  
  * 读取 / 保存 token
* `src/auth/config/api.ts`  
  * API_BASE，用于拼接请求地址

##### 五、已登录应用布局 + 导航 + Logout

**功能**  
登录后的应用区域：  
统一布局壳、  
桌面 / 移动端导航、  
当前用户信息展示、  
登出并跳转 Cognito logout。

**文件组合**

* `src/layouts/AppLayout.vue`  
  * 登录后页面壳  
  * Header + `<router-view />`
* `src/layouts/app/AppHeader.vue`  
  * 桌面导航  
  * Logout 按钮  
  * 移动端菜单入口
* `src/layouts/app/AppMobileNav.vue`  
  * 移动端导航列表
* `src/layouts/app/nav.ts`  
  * 导航项配置（当前仅 Me）
* `src/layouts/app/useAppLayout.ts`  
  * 聚合逻辑：  
    * 当前用户显示名  
    * userInfo 拉取协调  
    * Logout 行为封装
* `src/auth/display/userLabel.ts`  
  * 从 userInfo / JWT 生成展示名
* `src/auth/userInfo.ts`  
  * Cognito userInfo 请求
* `src/auth/storage/userInfoStorage.ts`  
  * userInfo 缓存
* `src/utils/jwt.ts`  
  * JWT payload 解码（UI 兜底）
* `src/auth/config/cognito.ts`  
  * Cognito logout 域名与 redirect 配置
* `src/auth/storage/tokenStorage.ts`  
  * 登出时清理 token

##### 六、受保护页面 `/app/me`（示例 API）

**功能**  
在登录后区域调用后端 `/me` 接口，  
展示返回结果或错误信息。

**文件组合**

* `src/pages/Me.vue`  
  * 页面 UI 与业务逻辑  
  * 调用 `authFetch("/me")`
* `src/auth/request/authFetch.ts`  
  * 自动携带 / 刷新 token
* `src/auth/config/api.ts`  
  * API_BASE 拼接请求地址
* `src/auth/storage/tokenStorage.ts`  
  * 提供 token（间接参与）

##### 七、登出回调落地页

**功能**  
Cognito logout redirect 后：  
展示“已登出”提示，  
提供回登录入口。

**文件组合**

* `src/pages/SignedOut.vue`  
  * 登出完成提示页 UI

##### 八、Auth 模块聚合入口

**功能**  
Auth 模块统一导出入口（barrel file），  
用于集中导出 auth 相关方法与配置。

**文件组合**

* `src/auth/index.ts`
* `src/auth/storage/index.ts`

---

### Step 7：AI / Codex 工作规则（必须遵守）

本节整合了 Codex Working Rules。

#### 总原则

* 代码质量优先于文件数量
* 不为凑文件而写代码

#### 交付粒度（Feature Slice）

* 每次输出 1 个功能切片
* 允许 3–8 个文件
* 超过必须拆 Part 1 / Part 2

#### 每个切片必须包含

* 修改 / 新增文件清单
* 每个文件完整内容
* 最小运行 / 验证说明

#### 不允许

* 改已冻结的 Cloud / Group 设计
* 引入新云服务 / 新角色
* Lambda 访问 MySQL
* Backend 直接操作 Cognito Group

#### 推荐切片顺序

1. Seller Submit 最小闭环
2. Admin Review 最小闭环
3. S3 presign（可选）

---

## 🔒 已冻结内容总览（索引）

以下内容 **已冻结**，禁止 AI / Codex 修改或重复实现：

* 架构与分层 → `docs/ARCHITECTURE.md`
* Cognito 结构与 Group → [Cognito & Authentication](#cognito--authentication)
* Serverless 资源 → [API Gateway & Lambda](#api-gateway--lambda)
* 当前完成进度 → [当前进度锚点](#step-3当前进度锚点动态)

---

## 🚧 尚未冻结内容（允许推进）

* Seller Onboarding 的完整前后端协作
* Admin 审核 Seller 的 UI / API 细节

📌 推进前必须：

* 明确写入文档
* 标记为「冻结」

---

## ✅ 给 AI / Codex 的一句话总结

> **README 决定你该读什么，
> ARCHITECTURE 决定你能不能动，
> CURRENT_PROGRESS 决定你现在该做什么。**
