package com.backend.flowershop.application.port.persistence;

import com.backend.flowershop.domain.model.UserAccount;
import java.util.Optional;

public interface UserAccountRepository {
    Optional<UserAccount> findBySubject(String subject);

    void save(UserAccount account);
}
