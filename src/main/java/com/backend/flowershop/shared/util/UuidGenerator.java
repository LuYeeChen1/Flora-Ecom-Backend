package com.backend.flowershop.shared.util;

import java.util.UUID;

/**
 * UuidGenerator：基于 UUID 的 IdGenerator 实现（冻结文件）

 * 职责边界：
 * 1) 唯一职责：生成 UUID 字符串
 * 2) 不包含业务逻辑
 * 3) 可在测试或未来需求中替换实现

 * 输出格式：
 * - 使用 UUID 标准字符串（包含 '-'）
 * - 若未来需要无 '-' 格式，应新增另一个实现，而不是修改本实现（避免破坏兼容）
 */
public class UuidGenerator implements IdGenerator {

    @Override
    public String newId() {
        return UUID.randomUUID().toString();
    }
}
