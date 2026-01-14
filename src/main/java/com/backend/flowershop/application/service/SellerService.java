package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.application.port.out.RoleTransitionPort;
import com.backend.flowershop.domain.enums.Role;         // 👈 引入
import com.backend.flowershop.domain.enums.SellerStatus; // 👈 引入
import com.backend.flowershop.domain.enums.SellerType;   // 👈 引入
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import com.backend.flowershop.domain.repository.UserRepository;
import com.backend.flowershop.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerProfileRepository sellerRepository;
    private final UserRepository userRepository;
    private final RoleTransitionPort roleTransitionPort;

    public SellerService(SellerProfileRepository sellerRepository,
                         UserRepository userRepository,
                         RoleTransitionPort roleTransitionPort) {
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
        this.roleTransitionPort = roleTransitionPort;
    }

    public Optional<String> getStatus(String userId) {
        return sellerRepository.findStatusByUserId(userId);
    }

    @Transactional
    public void applyForSeller(String userId, SellerApplyDTORequest request) {
        // 1. 幂等性校验
        Optional<String> status = sellerRepository.findStatusByUserId(userId);
        // ✅ 使用 Enum 比较，防止拼写错误
        if (status.isPresent() && !SellerStatus.NONE.name().equals(status.get())) {
            throw new IllegalStateException("您已有有效的契约，无法重复提交。");
        }

        // 2. 写入数据库
        // ✅ 使用 SellerType Enum 进行逻辑判断
        if (SellerType.INDIVIDUAL.name().equalsIgnoreCase(request.getApplyType())) {
            sellerRepository.saveIndividual(userId, request);
        } else {
            sellerRepository.saveBusiness(userId, request);
        }

        // 3. 触发 Lambda
        roleTransitionPort.promoteToSeller(userId);

        // 4. 更新本地用户角色
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));

        // ✅ 使用 Enum 设置，强类型安全！
        user.setRole(Role.SELLER);

        userRepository.save(user);
    }
}