# FlowerShop

FlowerShop 是一个以 **0 成本 / Free Tier** 为硬约束的电商后端项目，
采用 **Cognito + Serverless + Spring Boot（Clean Architecture）** 的组合架构。

本仓库当前 **只作为后端基础与规范锚点** 使用。

---

## 🔒 冻结事实（重要）

以下内容为 **已确认最终状态**，禁止 AI / Codex 重复实现、修改或推翻：

### ✅ 已完成（不要再做）
- 前端认证：
    - Cognito Hosted UI
    - PKCE（S256）
    - 登录 / 登出 / token refresh
- Cognito：
    - User Pool 已创建
    - 唯一 App Client：`flowershop-spa`
    - Groups：ADMIN / CUSTOMER / SELLER
    - Custom Attribute：`custom:role_stage`
- Lambda（已部署）：
    - `flowershop-pre-signup-validate`（空实现）
    - `flowershop-post-confirmation-init`
    - `flowershop-seller-finalize`
- API Gateway：
    - HTTP API + JWT Authorizer
    - `POST /seller/finalize`

### ❌ 尚未开始 / 将从 0 重做
- Spring Boot 后端代码
- 本地 MySQL 业务数据模型
- Seller Onboarding 本地业务闭环

---

## ⚠️ AI 行为约束（摘要）

- 本仓库 **禁止任何自动写入行为**
- 所有代码必须以 **Copy & Paste** 方式由人类手动加入
- 后端 package / file 结构必须 **先设计并冻结**

详细规则见：
- `docs/STATUS.md`
- `docs/ARCHITECTURE.md`
- `docs/AI_CONTRACT.md`
