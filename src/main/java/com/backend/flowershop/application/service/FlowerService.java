package com.backend.flowershop.application.service;

import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowerService {

    private final FlowerRepository flowerRepository;

    public FlowerService(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    public List<Flower> getPublicFlowerCatalog() {
        // 将来可以在这里添加业务逻辑，比如：只返回库存 > 0 的花
        return flowerRepository.findAllPublic();
    }
}