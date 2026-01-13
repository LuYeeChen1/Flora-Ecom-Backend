package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.FlowerDTO; // 导入 DTO
import com.backend.flowershop.application.service.FlowerService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/public/flowers")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicFlowerController {

    private final FlowerService flowerService;

    public PublicFlowerController(FlowerService flowerService) {
        this.flowerService = flowerService;
    }

    // 返回类型变为 List<FlowerDTO>
    @GetMapping
    public List<FlowerDTO> getAllFlowers() {
        return flowerService.getPublicFlowerCatalog();
    }
}