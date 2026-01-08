# Backend Architecture – FlowerShop

## 总体原则（冻结）

- 架构：Clean Architecture
- 分层（强制）：
    - interfaces (controller)
    - application (service / use case / pipeline)
    - domain (model / rule interface)
    - infrastructure (port implementation)
- 依赖方向：
    - interfaces → application → domain
    - infrastructure 只实现 port，不反向依赖

---

## Rule 系统（强制）

- rules：
    - 只允许 interface
- rule-impl：
    - 所有 validate 必须在这里
    - 每个实现必须使用 `@Order(X)`
    - 文件命名：`{Prefix}{X三位数}`（如 AT010）

---

## 技术约束

- Controller 必须最薄
- validate 禁止出现在：
    - controller
    - service
    - normalize
- 能用 record 就用
- 能用 @Component / @Bean 就用
