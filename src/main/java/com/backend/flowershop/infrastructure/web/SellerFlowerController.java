package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
import com.backend.flowershop.application.port.out.StoragePort;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import com.backend.flowershop.infrastructure.persistence.JdbcFlowerRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.List;
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

    // 1. 获取上传链接 (S3 Presigned URL)
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

    // 3. 获取我的库存 (Dashboard 使用)
    @GetMapping
    public ResponseEntity<List<Flower>> getMyInventory(@AuthenticationPrincipal Jwt jwt) {
        String sellerId = jwt.getSubject();

        // 强制转换以使用 JdbcFlowerRepository 特有的 findAllBySellerId 方法
        // 注意：请确保 JdbcFlowerRepository 中已经实现了此方法
        List<Flower> myFlowers = ((JdbcFlowerRepository)flowerRepository).findAllBySellerId(sellerId);

        return ResponseEntity.ok(myFlowers);
    }
}