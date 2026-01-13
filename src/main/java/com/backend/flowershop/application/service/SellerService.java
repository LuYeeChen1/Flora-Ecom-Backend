package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.domain.model.SellerProfile;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellerService {

    private final SellerProfileRepository sellerRepository;

    public SellerService(SellerProfileRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * 处理卖家申请
     * @param userId 从 Token 解析出的用户 ID (Cognito sub)
     * @param request 前端提交的申请数据
     */
    @Transactional
    public void applyForSeller(String userId, SellerApplyDTORequest request) {
        // 1. DTO 转 Entity
        SellerProfile profile = new SellerProfile();
        profile.setUserId(userId);
        profile.setRealName(request.getRealName());
        profile.setIdCardNumber(request.getIdCardNumber());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setBusinessAddress(request.getBusinessAddress());

        // 2. 强制设置状态为 "待审核"
        profile.setStatus("PENDING_REVIEW");

        // 3. 保存至数据库
        sellerRepository.save(profile);
    }
}