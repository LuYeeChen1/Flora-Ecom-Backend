package com.backend.flowershop.interfaces.controller.me.mapper;

import com.backend.flowershop.interfaces.controller.me.dto.MeResponse;
import com.backend.flowershop.interfaces.security.AuthPrincipal;
import org.springframework.stereotype.Component;

/**
 * 作用：Me 的 DTO 映射
 * 边界：只做转换
 */
@Component
public class MeMapper {

    public MeResponse toResponse(AuthPrincipal p) {
        return new MeResponse(p.userSub(), p.email(), p.groups(), p.roleStage());
    }
}
