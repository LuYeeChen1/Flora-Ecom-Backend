package com.backend.flowershop.shared.error;

/**
 * NotFoundException：资源不存在异常（冻结文件）

 * 职责边界：
 * 1) 表示“业务上期望存在，但实际不存在”的资源
 * 2) 统一映射为 HTTP 404
 * 3) 不用于权限问题、不用于校验问题

 * 典型使用场景：
 * - 根据当前用户查询 SellerProfile，但数据库不存在
 * - 根据 ID 查询业务实体，但记录未找到

 * 注意：
 * - Repository 层可以返回 Optional.empty()
 * - 是否抛 NotFoundException，由 Application Service 决定
 */
public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
