# Project Status – FlowerShop Backend

> 本文件是 **项目进度的唯一可信来源**  
> AI / Codex 不得通过“猜测文件意图”来推断进度

---

## 🧭 当前阶段（Frozen）

### Phase 0 – Infrastructure & Auth（✅ 已完成）
- Cognito User Pool / App Client
- Cognito Groups & role_stage 状态机
- Lambda + API Gateway
- 前端登录 / 登出 / token 流程

👉 **禁止 AI 再次实现、修改或模拟上述内容**

---

### Phase 1 – Backend Rebuild（🚧 正在进行）
- Spring Boot 后端 **从 0 重做**
- 目标：
    - Clean Architecture
    - 最小可运行闭环（MVP）
- 当前状态：
    - ❌ 任何后端代码都不被视为“最终”
    - ❌ package / file 结构尚未冻结

👉 **AI 的第一步任务：只允许输出 package & file 设计**

---

### Phase 2 – Local Business Loop（未开始）
- 本地 MySQL
- Seller Onboarding 本地流程
- 前端 → 本地后端 API

---

### Phase 3 – Cloud Finalize Verification（未开始）
- 前端触发 `/seller/finalize`
- Cognito 状态与 Group 验证

---

## 🚫 明确禁止的 AI 行为

- ❌ 根据现有代码“猜测”完成度
- ❌ 假设 backend 已存在稳定实现
- ❌ 跳过 Phase 顺序
- ❌ 直接输出大量业务代码（未冻结结构前）

---

## ✅ AI 允许的行为（当前阶段）

- ✔ 只读分析仓库
- ✔ 输出后端 package / file 设计
- ✔ 等待人工确认后继续
