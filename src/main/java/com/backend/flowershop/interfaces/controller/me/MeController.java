package com.backend.flowershop.interfaces.controller.me;

import com.backend.flowershop.application.port.in.me.GetMeUseCase;
import com.backend.flowershop.interfaces.controller.me.dto.MeResponse;
import com.backend.flowershop.interfaces.controller.me.mapper.MeMapper;
import com.backend.flowershop.interfaces.security.AuthPrincipal;
import com.backend.flowershop.interfaces.security.AuthPrincipalExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作用：/me 接口
 * 边界：Controller 最薄，只做映射/提取 principal/DTO 转换
 */
@RestController
public class MeController {

    private final GetMeUseCase getMeUseCase;
    private final AuthPrincipalExtractor principalExtractor;
    private final MeMapper mapper;

    public MeController(
            GetMeUseCase getMeUseCase,
            AuthPrincipalExtractor principalExtractor,
            MeMapper mapper
    ) {
        this.getMeUseCase = getMeUseCase;
        this.principalExtractor = principalExtractor;
        this.mapper = mapper;
    }

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        AuthPrincipal p = principalExtractor.extract(authentication);
        AuthPrincipal result = getMeUseCase.execute(p);
        return mapper.toResponse(result);
    }
}
