# FlowerShop

FlowerShop 是一个以 **0 成本 / Free Tier** 为硬约束的电商后端项目，
采用 **Cognito + Serverless + Spring Boot（Clean Architecture）** 的组合架构。

本仓库用于承载 **FlowerShop 后端代码、架构规范与阶段性进度锚点**。

> ⚠️ AI / Codex **禁止假设未在 STATUS.md 标明的内容已经完成**  
> 项目进度以 `docs/STATUS.md` 为唯一可信来源。

---

## 🔒 冻结事实（重要）

以下内容为 **已确认最终状态**，禁止 AI / Codex 重复实现、修改或推翻。

### ✅ 已完成（冻结）

#### 前端认证
- Cognito Hosted UI
- PKCE（S256）
- 登录 / 登出 / token refresh
- access_token 用于调用后端 API

#### Cognito
- User Pool 已创建
- 唯一 App Client：`flowershop-spa`
- Groups：
    - ADMIN
    - CUSTOMER（默认）
    - SELLER
- Custom Attribute：
    - `custom:role_stage`

#### Serverless（冻结）
- Lambda：
    - `flowershop-pre-signup-validate`（空实现）
    - `flowershop-post-confirmation-init`
    - `flowershop-seller-finalize`
- API Gateway：
    - HTTP API
    - JWT Authorizer（Issuer + Audience）
    - Route：`POST /seller/finalize`

---

## 🏗️ 后端当前状态（重要）

- Spring Boot 后端已完成：
    - Clean Architecture 分层
    - JWT Resource Server（Cognito）
    - audience(client_id) 校验
    - Cognito Groups → Spring Security ROLE 映射
    - 统一异常输出
    - Rule / RuleImpl 校验体系
    - Seller Onboarding 本地业务闭环（JDBC + MySQL）
- 数据库：
    - 本地 MySQL（手动建表，JDBC）

---

## 🧠 AI 行为约束（摘要）

- 本仓库 **禁止任何自动写入行为**
- 所有代码必须由人类 **Copy & Paste**
- 允许 AI：
    - 设计
    - 审计
    - 输出文本代码
- 禁止 AI：
    - 推翻冻结结构
    - 假设未声明完成的功能已存在

详细规则见：
- `docs/STATUS.md`
- `docs/ARCHITECTURE.md`
- `docs/AI_CONTRACT.md`
