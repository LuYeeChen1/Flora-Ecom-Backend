package com.backend.flowershop.domain.model.seller;

import com.backend.flowershop.domain.model.common.Address;
import com.backend.flowershop.domain.model.common.Email;
import com.backend.flowershop.domain.model.common.PhoneNumber;
import com.backend.flowershop.domain.model.user.CognitoSub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * SellerProfile：卖家资料聚合/实体（冻结文件）

 * 职责边界：
 * 1) 表达卖家 onboarding 所需的业务资料（公司信息、联系人、地址、文件等）
 * 2) 不依赖数据库实现（JPA/JDBC 都不在这里）
 * 3) 不做复杂业务校验（规则由 application/rules + pipeline 处理）
 * 4) 保持不可变倾向：对外只暴露只读视图；修改通过明确方法表达意图

 * 重要设计点：
 * - ownerSub：与 Cognito 用户绑定（JWT sub），用作身份锚点
 * - stage：SellerProfile 在业务库的流程状态（SellerStage）
 */
public final class SellerProfile {

    private final SellerId sellerId;
    private final CognitoSub ownerSub;

    private final String shopName;
    private final String companyName;

    private final Email contactEmail;
    private final PhoneNumber contactPhone;

    private final Address businessAddress;

    private final SellerStage stage;

    private final List<SellerDocument> documents;

    public SellerProfile(
            SellerId sellerId,
            CognitoSub ownerSub,
            String shopName,
            String companyName,
            Email contactEmail,
            PhoneNumber contactPhone,
            Address businessAddress,
            SellerStage stage,
            List<SellerDocument> documents
    ) {
        if (sellerId == null) throw new IllegalArgumentException("sellerId 不能为空");
        if (ownerSub == null) throw new IllegalArgumentException("ownerSub 不能为空");
        if (shopName == null || shopName.isBlank()) throw new IllegalArgumentException("shopName 不能为空");
        if (companyName == null || companyName.isBlank()) throw new IllegalArgumentException("companyName 不能为空");
        if (contactEmail == null) throw new IllegalArgumentException("contactEmail 不能为空");
        if (contactPhone == null) throw new IllegalArgumentException("contactPhone 不能为空");
        if (businessAddress == null) throw new IllegalArgumentException("businessAddress 不能为空");
        if (stage == null) throw new IllegalArgumentException("stage 不能为空");

        this.sellerId = sellerId;
        this.ownerSub = ownerSub;

        this.shopName = shopName.trim();
        this.companyName = companyName.trim();

        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;

        this.businessAddress = businessAddress;

        this.stage = stage;

        List<SellerDocument> safeDocs = (documents == null) ? new ArrayList<>() : new ArrayList<>(documents);
        this.documents = Collections.unmodifiableList(safeDocs);
    }

    public SellerId sellerId() { return sellerId; }
    public CognitoSub ownerSub() { return ownerSub; }

    public String shopName() { return shopName; }
    public String companyName() { return companyName; }

    public Email contactEmail() { return contactEmail; }
    public PhoneNumber contactPhone() { return contactPhone; }

    public Address businessAddress() { return businessAddress; }

    public SellerStage stage() { return stage; }

    public List<SellerDocument> documents() { return documents; }

    /**
     * 领域意图：更新资料（具体允许不允许由 application/rules 决定）
     * 这里仅表达“可以生成一个新版本”
     */
    public SellerProfile withUpdatedProfile(
            String shopName,
            String companyName,
            Email contactEmail,
            PhoneNumber contactPhone,
            Address businessAddress,
            List<SellerDocument> documents
    ) {
        return new SellerProfile(
                this.sellerId,
                this.ownerSub,
                shopName,
                companyName,
                contactEmail,
                contactPhone,
                businessAddress,
                this.stage,
                documents
        );
    }

    /**
     * 领域意图：改变 stage（合法性由 SellerStagePolicy 决定）
     */
    public SellerProfile withStage(SellerStage newStage) {
        return new SellerProfile(
                this.sellerId,
                this.ownerSub,
                this.shopName,
                this.companyName,
                this.contactEmail,
                this.contactPhone,
                this.businessAddress,
                newStage,
                this.documents
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SellerProfile)) return false;
        SellerProfile that = (SellerProfile) o;
        return Objects.equals(sellerId, that.sellerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerId);
    }

    @Override
    public String toString() {
        return "SellerProfile{" +
                "sellerId=" + sellerId +
                ", ownerSub=" + ownerSub +
                ", shopName='" + shopName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", stage=" + stage +
                '}';
    }
}
