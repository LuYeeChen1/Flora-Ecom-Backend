package com.backend.flowershop.interfaces.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作用：从 Spring Security Authentication/JWT 提取 AuthPrincipal
 * 边界：只做提取与转换，不做业务校验
 */
@Component
public class AuthPrincipalExtractor {

    public AuthPrincipal extract(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken token)) {
            return new AuthPrincipal(null, null, Set.of(), null);
        }

        Jwt jwt = token.getToken();

        String sub = jwt.getClaimAsString(JwtClaimKeys.SUB);
        String email = jwt.getClaimAsString(JwtClaimKeys.EMAIL);
        String roleStage = jwt.getClaimAsString(JwtClaimKeys.ROLE_STAGE);

        Set<String> groups = new HashSet<>();
        Object raw = jwt.getClaims().get(JwtClaimKeys.GROUPS);
        if (raw instanceof List<?> list) {
            for (Object o : list) {
                if (o != null) groups.add(String.valueOf(o));
            }
        }

        if (groups.isEmpty()) {
            groups = Collections.emptySet();
        }

        return new AuthPrincipal(sub, email, groups, roleStage);
    }
}
