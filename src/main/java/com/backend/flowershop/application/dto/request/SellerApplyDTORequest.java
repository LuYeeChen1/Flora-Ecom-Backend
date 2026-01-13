package com.backend.flowershop.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SellerApplyDTORequest {

    @NotBlank(message = "Real name is required")
    @Size(min = 2, max = 100)
    private String realName;

    @NotBlank(message = "ID number is required")
    private String idCardNumber; // 身份证/营业执照号

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Invalid phone format")
    private String phoneNumber;

    @NotBlank(message = "Business address is required")
    private String businessAddress;

    // Getters and Setters
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getIdCardNumber() { return idCardNumber; }
    public void setIdCardNumber(String idCardNumber) { this.idCardNumber = idCardNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBusinessAddress() { return businessAddress; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }
}