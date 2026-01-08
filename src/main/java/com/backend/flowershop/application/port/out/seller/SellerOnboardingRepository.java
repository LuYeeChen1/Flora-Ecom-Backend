package com.backend.flowershop.application.port.out.seller;

import com.backend.flowershop.domain.model.seller.SellerOnboarding;

import java.util.Optional;

/**
 * 作用：Seller Onboarding 持久化抽象（Port）
 * 边界：application 依赖此接口，infrastructure 提供实现
 */
public interface SellerOnboardingRepository {
    SellerOnboarding save(SellerOnboarding onboarding);
    Optional<SellerOnboarding> findByUserSub(String userSub);
}
