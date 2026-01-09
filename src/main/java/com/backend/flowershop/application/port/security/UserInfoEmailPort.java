package com.backend.flowershop.application.port.security;

import java.util.Optional;

public interface UserInfoEmailPort {
    Optional<String> fetchEmail(String accessToken);
}
