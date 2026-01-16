package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.response.FlowerDTOResponse;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Value; // 必须引入 Value
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowerService {

    private final FlowerRepository flowerRepository;

    // 1. 注入 S3 基础 URL (与 SellerService 保持一致)
    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public FlowerService(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    public List<FlowerDTOResponse> getPublicFlowerCatalog() {
        return flowerRepository.findAllPublic()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // 辅助映射方法
    private FlowerDTOResponse toResponse(Flower flower) {
        // ✅ 2. 定义并计算 fullUrl 变量
        String fullUrl = flower.getImageUrl();
        if (fullUrl != null && !fullUrl.startsWith("http")) {
            fullUrl = s3BaseUrl + fullUrl;
        }

        return new FlowerDTOResponse(
                String.valueOf(flower.getId()),
                flower.getName(),
                flower.getDescription(),
                flower.getPrice(),
                flower.getStock(),
                fullUrl,
                flower.getCategory()
        );
    }
}