package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.response.FlowerDTOResponse; // Updated Import
import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.application.service.FlowerService;
import com.backend.flowershop.infrastructure.persistence.JdbcFlowerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/public/flowers")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicFlowerController {

    private final FlowerService flowerService;
    private final JdbcFlowerRepository flowerRepository; // 注入 Repo

    public PublicFlowerController(FlowerService flowerService, JdbcFlowerRepository flowerRepository) {
        this.flowerService = flowerService;
        this.flowerRepository = flowerRepository;
    }

    @GetMapping
    public List<FlowerDTOResponse> getAllFlowers() {
        // 确保 Service 里的 DTO 转换逻辑也加上了 S3 Base URL 前缀
        return flowerService.getPublicFlowerCatalog();
    }

    //获取详情
    @GetMapping("/{id}")
    public ResponseEntity<FlowerDetailDTOResponse> getFlowerDetail(@PathVariable Long id) {
        return flowerRepository.findDetailById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}