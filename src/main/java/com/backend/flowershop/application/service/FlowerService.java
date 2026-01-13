package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.FlowerDTO;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowerService {

    private final FlowerRepository flowerRepository;

    public FlowerService(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    public List<FlowerDTO> getPublicFlowerCatalog() {
        // 1. 获取领域实体列表
        List<Flower> flowers = flowerRepository.findAllPublic();

        // 2. 转换逻辑：List<Entity> -> List<DTO>
        return flowers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 简单的内部映射方法
    private FlowerDTO toDTO(Flower flower) {
        return new FlowerDTO(
                flower.getId(),
                flower.getName(),
                flower.getDescription(),
                flower.getPrice(),
                flower.getImageUrl(),
                flower.getCategory()
        );
    }
}