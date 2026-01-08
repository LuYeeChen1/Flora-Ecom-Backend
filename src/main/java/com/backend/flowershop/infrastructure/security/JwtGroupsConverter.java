package com.backend.flowershop.infrastructure.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 作用：
 * - 把 Cognito 的 cognito:groups claim 转成 Spring Security Authorities

 * 映射规则：
 * - CUSTOMER -> ROLE_CUSTOMER
 * - SELLER   -> ROLE_SELLER
 * - ADMIN    -> ROLE_ADMIN
 */
public class JwtGroupsConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String GROUPS_CLAIM = "cognito:groups";

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Object raw = jwt.getClaims().get(GROUPS_CLAIM);
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (raw instanceof List<?> groups) {
            for (Object g : groups) {
                if (g == null) continue;
                String role = String.valueOf(g).toUpperCase();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }

        return authorities;
    }
}
