package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellerService {

    private final SellerProfileRepository sellerRepository;

    public SellerService(SellerProfileRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Transactional
    public void applyForSeller(String userId, SellerApplyDTORequest request) {
        if ("INDIVIDUAL".equalsIgnoreCase(request.getApplyType())) {
            // 简单校验
            if (request.getRealName() == null || request.getNricNumber() == null) {
                throw new IllegalArgumentException("Real Name and NRIC are required for Individuals");
            }
            sellerRepository.saveIndividual(userId, request);

        } else if ("BUSINESS".equalsIgnoreCase(request.getApplyType())) {
            // 简单校验
            if (request.getCompanyName() == null || request.getBrnNumber() == null || request.getTinNumber() == null) {
                throw new IllegalArgumentException("Company Name, BRN and TIN are required for Business");
            }
            sellerRepository.saveBusiness(userId, request);

        } else {
            throw new IllegalArgumentException("Invalid Apply Type. Must be INDIVIDUAL or BUSINESS");
        }
    }
}