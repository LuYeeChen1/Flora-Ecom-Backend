package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.service.UserService;
import com.backend.flowershop.domain.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    // ✅ 注入 Service，不再接触 Repository
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public User syncAndUserProfile(@AuthenticationPrincipal Jwt jwt) {
        // Controller 职责：仅负责从 Web 协议 (JWT) 中提取数据
        String userId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("username");

        // 提取原始 Group 列表
        java.util.List<String> groups = jwt.getClaimAsStringList("cognito:groups");

        // 委派给 Service 处理核心业务
        return userService.syncCognitoUser(userId, email, username, groups);
    }
}