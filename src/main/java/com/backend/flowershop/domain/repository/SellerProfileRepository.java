package com.backend.flowershop.domain.repository;

import com.backend.flowershop.domain.model.SellerProfile;
import java.util.Optional;

public interface SellerProfileRepository {
    // 保存或更新卖家资料
    void save(SellerProfile profile);

    // 根据用户ID查询资料
    Optional<SellerProfile> findByUserId(String userId);
}