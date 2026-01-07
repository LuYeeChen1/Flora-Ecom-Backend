package com.backend.flowershop.shared.time;

import java.time.Instant;

/**
 * ClockProvider：时间提供者抽象（冻结文件）

 * 职责边界：
 * 1) 统一提供“当前时间”的抽象入口
 * 2) 业务层 / Application Service 只能依赖这个接口
 * 3) 禁止在业务代码中直接使用 Instant.now() / LocalDateTime.now()

 * 为什么要这样做：
 * - 方便单元测试（可注入固定时间）
 * - 方便未来审计 / 事件回放
 * - 避免“隐式依赖系统时间”的不可控行为

 * 输出：
 * - 当前时间（Instant，时区无关，适合存储与比较）
 */
public interface ClockProvider {

    Instant now();
}
