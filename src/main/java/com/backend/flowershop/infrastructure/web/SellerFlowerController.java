package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
import com.backend.flowershop.application.port.out.StoragePort;
import com.backend.flowershop.domain.repository.FlowerRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/seller/flowers")
public class SellerFlowerController {

    private final StoragePort storagePort;
    private final FlowerRepository flowerRepository;

    public SellerFlowerController(StoragePort storagePort, FlowerRepository flowerRepository) {
        this.storagePort = storagePort;
        this.flowerRepository = flowerRepository;
    }

    // 1. 获取上传链接
    @GetMapping("/upload-url")
    public ResponseEntity<Map<String, String>> getPresignedUrl(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("contentType") String contentType,
            @RequestParam("fileName") String fileName
    ) {
        String sellerId = jwt.getSubject();
        // 构造唯一路径: flowers/用户ID/UUID_文件名
        String key = "flowers/" + sellerId + "/" + UUID.randomUUID() + "_" + fileName;

        URL url = storagePort.generatePresignedUrl(key, contentType);

        return ResponseEntity.ok(Map.of(
                "uploadUrl", url.toString(),
                "key", key
        ));
    }

    // 2. 保存鲜花信息
    @PostMapping
    public ResponseEntity<?> createFlower(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody FlowerDTORequest request
    ) {
        String sellerId = jwt.getSubject();
        flowerRepository.save(sellerId, request);
        return ResponseEntity.ok("Flower listed successfully.");
    }
}