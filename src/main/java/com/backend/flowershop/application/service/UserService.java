package com.backend.flowershop.application.service;

import com.backend.flowershop.domain.model.User;
import com.backend.flowershop.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // 依赖注入的是接口，不是 JDBC 实现类
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Use Case: 同步 Cognito 用户到本地数据库
     * 包含业务逻辑：角色解析、实体构建
     */
    public User syncCognitoUser(String id, String email, String username, List<String> cognitoGroups) {
        // 1. 业务逻辑：解析角色 (默认 CUSTOMER)
        String role = "CUSTOMER";
        if (cognitoGroups != null && !cognitoGroups.isEmpty()) {
            // 这里可以加入更复杂的映射逻辑，比如把 "admin-group" 映射为 "ADMIN"
            role = cognitoGroups.get(0);
        }

        // 2. 业务逻辑：构建领域对象
        User user = new User(id, email, username, role);

        // 3. 数据持久化
        userRepository.save(user);

        return user;
    }
}