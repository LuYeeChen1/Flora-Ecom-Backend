package com.backend.flowershop.interfaces.controller.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 作用：健康检查
 * 边界：匿名可访问，不做任何业务逻辑
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "ok");
    }
}
