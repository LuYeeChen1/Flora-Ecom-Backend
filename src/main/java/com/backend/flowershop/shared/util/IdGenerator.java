package com.backend.flowershop.shared.util;

/**
 * IdGenerator：ID 生成器抽象（冻结文件）

 * 职责边界：
 * 1) 统一提供生成“新 ID”的能力
 * 2) 业务层 / Application 层只依赖接口，不依赖具体实现
 * 3) 未来如需切换为 Snowflake / ULID / 数据库自增 等，不影响业务代码

 * 输出：
 * - 新生成的 ID（String 形式，避免把 UUID 类型泄露到各层）
 */
public interface IdGenerator {

    String newId();
}
