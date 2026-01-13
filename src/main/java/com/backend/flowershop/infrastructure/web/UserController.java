package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.UserDTO;
import com.backend.flowershop.application.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 返回类型变为 UserDTO
    @GetMapping("/me")
    public UserDTO syncAndUserProfile(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("username");
        java.util.List<String> groups = jwt.getClaimAsStringList("cognito:groups");

        return userService.syncCognitoUser(userId, email, username, groups);
    }
}