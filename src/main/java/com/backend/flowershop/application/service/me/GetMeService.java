package com.backend.flowershop.application.service.me;

import com.backend.flowershop.application.port.in.me.GetMeUseCase;
import com.backend.flowershop.interfaces.security.AuthPrincipal;
import org.springframework.stereotype.Service;

/**
 * 作用：获取当前用户信息（/me）
 * 边界：最小实现：直接回传 principal（未来可扩展读取业务库）
 */
@Service
public class GetMeService implements GetMeUseCase {

    @Override
    public AuthPrincipal execute(AuthPrincipal principal) {
        return principal;
    }
}
