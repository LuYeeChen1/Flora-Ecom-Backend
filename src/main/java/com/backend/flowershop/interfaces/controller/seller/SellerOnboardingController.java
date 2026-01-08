package com.backend.flowershop.interfaces.controller.seller;

import com.backend.flowershop.application.port.in.seller.GetMySellerOnboardingStatusUseCase;
import com.backend.flowershop.application.port.in.seller.GetMySellerOnboardingUseCase;
import com.backend.flowershop.application.port.in.seller.SubmitSellerOnboardingUseCase;
import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import com.backend.flowershop.interfaces.controller.seller.dto.SellerOnboardingResponse;
import com.backend.flowershop.interfaces.controller.seller.dto.SellerOnboardingStatusResponse;
import com.backend.flowershop.interfaces.controller.seller.dto.SubmitSellerOnboardingRequest;
import com.backend.flowershop.interfaces.controller.seller.mapper.SellerOnboardingMapper;
import com.backend.flowershop.interfaces.security.AuthPrincipal;
import com.backend.flowershop.interfaces.security.AuthPrincipalExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 作用：Seller Onboarding HTTP 入口
 * 边界：Controller 最薄，只做 HTTP 映射 / principal 提取 / DTO 转换
 */
@RestController
@RequestMapping("/seller/onboarding")
public class SellerOnboardingController {

    private final AuthPrincipalExtractor principalExtractor;
    private final SellerOnboardingMapper mapper;
    private final SubmitSellerOnboardingUseCase submitUseCase;
    private final GetMySellerOnboardingUseCase getUseCase;
    private final GetMySellerOnboardingStatusUseCase statusUseCase;

    public SellerOnboardingController(
            AuthPrincipalExtractor principalExtractor,
            SellerOnboardingMapper mapper,
            SubmitSellerOnboardingUseCase submitUseCase,
            GetMySellerOnboardingUseCase getUseCase,
            GetMySellerOnboardingStatusUseCase statusUseCase
    ) {
        this.principalExtractor = principalExtractor;
        this.mapper = mapper;
        this.submitUseCase = submitUseCase;
        this.getUseCase = getUseCase;
        this.statusUseCase = statusUseCase;
    }

    @PostMapping("/submit")
    public SellerOnboardingResponse submit(
            Authentication authentication,
            @RequestBody SubmitSellerOnboardingRequest request
    ) {
        AuthPrincipal p = principalExtractor.extract(authentication);
        SellerOnboarding saved = submitUseCase.execute(p, mapper.toProfile(request), mapper.toDocuments(request));
        return mapper.toResponse(saved);
    }

    @GetMapping("/me")
    public SellerOnboardingResponse me(Authentication authentication) {
        AuthPrincipal p = principalExtractor.extract(authentication);
        SellerOnboarding onboarding = getUseCase.execute(p);
        return mapper.toResponse(onboarding);
    }

    @GetMapping("/status")
    public SellerOnboardingStatusResponse status(Authentication authentication) {
        AuthPrincipal p = principalExtractor.extract(authentication);
        return statusUseCase.execute(p);
    }
}
