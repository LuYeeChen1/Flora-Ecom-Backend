# Project Status – FlowerShop Backend

> 本文件是 **项目进度的唯一可信来源**  
> AI / Codex **禁止通过“猜测文件意图”来判断进度**

---

## 🧭 当前阶段（Frozen Priority）

### Phase 0 – Infrastructure & Auth（✅ 已完成）

- Cognito User Pool / App Client
- Cognito Groups：
    - ADMIN
    - CUSTOMER（默认）
    - SELLER
- Cognito Custom Attribute：
    - `custom:role_stage`
- Lambda：
    - `flowershop-pre-signup-validate`
    - `flowershop-post-confirmation-init`
    - `flowershop-seller-finalize`
- API Gateway：
    - HTTP API
    - JWT Authorizer（Issuer + Audience）
    - Route：`POST /seller/finalize`
- 前端：
    - Hosted UI
    - PKCE
    - access_token 流程

🚫 **禁止 AI 再次实现、修改或模拟 Phase 0 内容**

---

### Phase 1 – Seller Registration MVP（🚧 进行中｜最高优先级）

> **这是第一个必须完成的端到端 MVP**  
> 在此 Phase 完成前，禁止开始任何其他业务功能（Order / Product / Wallet 等）

#### 目标（必须全部满足）
- Seller 注册流程 **端到端闭环**：
    1. Frontend（Customer 登录）
    2. Frontend 提交 Seller Onboarding 表单
    3. Backend：
        - Clean Architecture
        - Rule / RuleImpl 校验
        - 本地 MySQL 持久化
    4. Frontend 查询 Seller 状态
    5. Frontend 触发 `/seller/finalize`
    6. Cloud：
        - Cognito `custom:role_stage` 更新为 `SELLER_ACTIVE`
        - CUSTOMER group → SELLER group
    7. 前端刷新 token 后状态正确

#### 当前已完成（冻结）
- Spring Boot 后端基础：
    - Clean Architecture 分层
    - JWT Resource Server（Cognito）
    - aud(client_id) 强校验
    - Cognito Groups → ROLE_* 映射
    - 统一异常 JSON
    - Rule / RuleImpl 校验体系
- Seller Onboarding **本地业务闭环**：
    - submit / query / status
    - JDBC + 本地 MySQL

#### 尚未完成（必须完成）
- 前端 Seller Onboarding 表单与状态页
- 前端调用 `/seller/finalize`
- Seller 完成后：
    - Cognito Group 实际变更验证
    - `custom:role_stage` 与本地状态一致性确认

🚫 **在 Phase 1 完成前，禁止：**
- 开始 Order / Product / Wallet 等功能
- 扩展 ADMIN 功能
- 重构 Seller 注册结构

---

### Phase 2 – Business Expansion（未开始｜被 Phase 1 阻塞）

> 仅在 **Seller Registration MVP 完整跑通并验证成功后** 才允许进入

可能包含：
- 商品管理（SELLER）
- 订单系统（CUSTOMER / SELLER）
- Seller 审核增强（REVIEWING / REJECTED 流）
- ADMIN 管理能力

---

### Phase 3 – Cloud Consistency & Hardening（未开始）

- 云端 / 本地状态一致性审计
- 异常补偿策略
- 权限边界安全复查

---

## 🚫 明确禁止的 AI 行为（强化）

- ❌ 在 Seller 注册 MVP 未完成前：
    - 设计或实现其他业务模块
- ❌ 假设 Seller 注册流程“已经完整跑通”
- ❌ 跳过 `/seller/finalize` 云端验证
- ❌ 推进 Phase 2 / Phase 3 内容

---

## ✅ 当前允许的 AI 行为（仅限）

- ✔ 协助完成 Seller Registration MVP
- ✔ 审计 Seller 注册端到端流程
- ✔ 修复 Seller 注册相关规则与边界
- ✔ 等待人工确认 Phase 1 完成
