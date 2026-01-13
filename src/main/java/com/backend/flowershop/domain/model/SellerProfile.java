package com.backend.flowershop.domain.model;

import java.time.LocalDateTime;

public class SellerProfile {
    private String userId;        // 对应 Cognito sub
    private String realName;      // 实名 [cite: 73]
    private String idCardNumber;  // 身份证 [cite: 73]
    private String phoneNumber;   // 电话 [cite: 73]
    private String businessAddress; // 地址 [cite: 73]
    private String status;        // PENDING, APPROVED, REJECTED
    private LocalDateTime appliedAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}