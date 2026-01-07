package com.backend.flowershop.domain.model.user;

/**
 * RoleGroup：业务角色组（冻结文件）

 * 职责边界：
 * 1) 领域层的“角色”定义（不依赖 Spring Security / JWT / Cognito SDK）
 * 2) 与 Cognito Groups 一一对应：ADMIN / CUSTOMER / SELLER
 * 3) 只表达“角色分类”，不包含任何权限判断逻辑（权限判断放 AuthorizationPort / 应用层）

 * 输入：无
 * 输出：角色组枚举值
 */
public enum RoleGroup {

    ADMIN,
    CUSTOMER,
    SELLER
}
