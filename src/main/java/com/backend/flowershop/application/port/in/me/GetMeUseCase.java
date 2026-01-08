package com.backend.flowershop.application.port.in.me;

import com.backend.flowershop.interfaces.security.AuthPrincipal;

/**
 * 作用：/me 用例入口
 * 输入：AuthPrincipal
 * 输出：AuthPrincipal（允许 service 统一处理后返回）
 */
public interface GetMeUseCase {
    AuthPrincipal execute(AuthPrincipal principal);
}
