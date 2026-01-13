package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.service.FlowerService;
import com.backend.flowershop.domain.model.Flower;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/public/flowers")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicFlowerController {

    private final FlowerService flowerService;

    // ✅ 注入 Service
    public PublicFlowerController(FlowerService flowerService) {
        this.flowerService = flowerService;
    }

    @GetMapping
    public List<Flower> getAllFlowers() {
        return flowerService.getPublicFlowerCatalog();
    }
}