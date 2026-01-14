package com.backend.flowershop.domain.repository;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import java.util.Optional;

// 引入 DTO 是为了配合 JdbcRepository 中的 saveIndividual/saveBusiness 实现
public interface SellerProfileRepository {

    /**
     * 保存个人花艺师申请 (写入 individual_sellers 表)
     * @param userId Cognito User ID
     * @param request 包含 realName, nricNumber, address 等字段
     */
    void saveIndividual(String userId, SellerApplyDTORequest request);

    /**
     * 保存注册商户申请 (写入 business_sellers 表)
     * @param userId Cognito User ID
     * @param request 包含 companyName, brn, tin, msic 等字段
     */
    void saveBusiness(String userId, SellerApplyDTORequest request);

    // 预留：未来如果需要读取状态，可以定义一个通用的返回对象或分别查询
    // Optional<SellerStatusDTO> findStatusByUserId(String userId);
}