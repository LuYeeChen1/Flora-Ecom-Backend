package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.application.port.out.RoleTransitionPort; // 👈 引入接口
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerProfileRepository sellerRepository;
    private final RoleTransitionPort roleTransitionPort; // 👈 注入接口

    // 构造函数注入 (Spring 会自动找到 ApiGatewayRoleTransitionAdapter)
    public SellerService(SellerProfileRepository sellerRepository, RoleTransitionPort roleTransitionPort) {
        this.sellerRepository = sellerRepository;
        this.roleTransitionPort = roleTransitionPort;
    }

    public Optional<String> getStatus(String userId) {
        return sellerRepository.findStatusByUserId(userId);
    }

    @Transactional
    public void applyForSeller(String userId, SellerApplyDTORequest request) {
        // 1. 幂等性校验
        Optional<String> status = sellerRepository.findStatusByUserId(userId);
        if (status.isPresent() && !"NONE".equals(status.get())) {
            throw new IllegalStateException("您已有有效的契约，无法重复提交。");
        }

        // 2. 写入本地数据库 (Core Business)
        // 这一步如果不报错，事务就会提交，状态变为 ACTIVE
        if ("INDIVIDUAL".equalsIgnoreCase(request.getApplyType())) {
            sellerRepository.saveIndividual(userId, request);
        } else {
            sellerRepository.saveBusiness(userId, request);
        }

        // 3. 🚀 触发云端权限变更 (Side Effect)
        // 只有当上面数据库操作成功后，才会走到这一步
        roleTransitionPort.promoteToSeller(userId);
    }
}