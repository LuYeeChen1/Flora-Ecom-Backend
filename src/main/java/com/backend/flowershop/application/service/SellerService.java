package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.application.port.out.RoleTransitionPort;
import com.backend.flowershop.domain.enums.Role;
import com.backend.flowershop.domain.enums.SellerStatus;
import com.backend.flowershop.domain.enums.SellerType;
import com.backend.flowershop.domain.model.User;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import com.backend.flowershop.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    /**
     * 核心交易逻辑：
     * 1. 检查状态
     * 2. 写入商家资料
     * 3. 调用 Lambda 修改 Cognito
     * 4. 更新本地用户角色
     * * @Transactional 保证原子性：只要任意一步报错（比如 Lambda 挂了），
     * 数据库里的商家资料和用户角色更新都会自动回滚，就像什么都没发生过一样。
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void applyForSeller(String userId, SellerApplyDTORequest request) {
        // 1. 幂等性校验
        Optional<String> status = sellerRepository.findStatusByUserId(userId);
        if (status.isPresent() && !SellerStatus.NONE.name().equals(status.get())) {
            throw new IllegalStateException("您已有有效的契约，无法重复提交。");
        }

        // 2. 写入本地数据库 (Core Business)
        if (SellerType.INDIVIDUAL.name().equalsIgnoreCase(request.getApplyType())) {
            sellerRepository.saveIndividual(userId, request);
        } else {
            sellerRepository.saveBusiness(userId, request);
        }

        // 3. 🚀 触发云端权限变更 (AWS Lambda -> Cognito)
        // 如果这里抛出异常，整个事务回滚
        roleTransitionPort.promoteToSeller(userId);

        // 4. 🔥 同步更新本地 Users 表的角色状态 🔥
        // 这一步是为了保持数据一致性。虽然 Token 还没刷新，但数据库必须先是对的。
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));

        user.setRole(Role.SELLER);
        userRepository.save(user);

        // 此时事务提交，数据库状态锁定为 SELLER
    }
}