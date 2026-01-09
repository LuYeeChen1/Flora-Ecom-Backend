package com.backend.flowershop.application.service;

import com.backend.flowershop.application.port.persistence.UserAccountRepository;
import com.backend.flowershop.application.port.security.UserInfoEmailPort;
import com.backend.flowershop.application.port.usecase.CurrentUserProfileUseCase;
import com.backend.flowershop.domain.error.DomainErrorException;
import com.backend.flowershop.domain.model.TokenClaims;
import com.backend.flowershop.domain.model.UserAccount;
import com.backend.flowershop.domain.model.UserProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserProfileService implements CurrentUserProfileUseCase {
    private static final String DOMAIN_ERROR_CODE = "DOMAIN_ERROR";

    private final UserAccountRepository userAccountRepository;
    private final UserInfoEmailPort userInfoEmailPort;

    public CurrentUserProfileService(UserAccountRepository userAccountRepository, UserInfoEmailPort userInfoEmailPort) {
        this.userAccountRepository = userAccountRepository;
        this.userInfoEmailPort = userInfoEmailPort;
    }

    @Override
    public UserProfile fetch(TokenClaims claims) {
        List<String> groups = claims.groups() == null ? List.of() : claims.groups();
        String email = userAccountRepository.findBySubject(claims.subject())
                .map(UserAccount::email)
                .orElseGet(() -> resolveAndCacheEmail(claims));
        return new UserProfile(
                claims.subject(),
                claims.username(),
                email,
                groups
        );
    }

    private String resolveAndCacheEmail(TokenClaims claims) {
        String email = normalizeEmail(claims.email());
        if (email == null) {
            String accessToken = normalizeAccessToken(claims.accessToken());
            email = userInfoEmailPort.fetchEmail(accessToken)
                    .map(this::normalizeEmail)
                    .orElse(null);
        }
        if (email == null) {
            throw new DomainErrorException(DOMAIN_ERROR_CODE, "Email not found for user.");
        }
        userAccountRepository.save(new UserAccount(claims.subject(), email));
        return email;
    }

    private String normalizeEmail(String email) {
        return Optional.ofNullable(email)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .orElse(null);
    }

    private String normalizeAccessToken(String accessToken) {
        return Optional.ofNullable(accessToken)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .orElseThrow(() -> new DomainErrorException(DOMAIN_ERROR_CODE, "Access token missing."));
    }
}
