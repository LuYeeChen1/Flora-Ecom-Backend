package com.backend.flowershop.domain.repository;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
import com.backend.flowershop.domain.model.Flower;
import java.util.List;

public interface FlowerRepository {
    // 获取公共鲜花列表 (现有功能)
    List<Flower> findAllPublic();

    // 保存鲜花 (关联 Seller ID)
    void save(String sellerId, FlowerDTORequest dto);
}