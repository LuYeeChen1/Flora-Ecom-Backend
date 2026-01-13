package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.UserDTO;
import com.backend.flowershop.domain.model.User;
import com.backend.flowershop.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Use Case: 同步 Cognito 用户
     * @return UserDTO (不再返回 User 实体)
     */
    public UserDTO syncCognitoUser(String id, String email, String username, List<String> cognitoGroups) {
        // 1. 业务逻辑：解析角色
        String role = "CUSTOMER";
        if (cognitoGroups != null && !cognitoGroups.isEmpty()) {
            role = cognitoGroups.get(0);
        }

        // 2. 业务处理：构建并保存领域实体
        User user = new User(id, email, username, role);
        userRepository.save(user);

        // 3. 转换逻辑：Domain Entity -> DTO
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole()
        );
    }
}