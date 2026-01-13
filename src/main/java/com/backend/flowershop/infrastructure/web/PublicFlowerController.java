package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.infrastructure.persistence.JdbcFlowerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/public/flowers") // 对应 Guest 访问路径 [cite: 85]
public class PublicFlowerController {

    private final JdbcFlowerRepository flowerRepository;

    public PublicFlowerController(JdbcFlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    @GetMapping
    public List<Flower> getAllFlowers() {
        return flowerRepository.findAllPublic();
    }
}