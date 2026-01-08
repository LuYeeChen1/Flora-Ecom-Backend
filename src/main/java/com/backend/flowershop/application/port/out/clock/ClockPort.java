package com.backend.flowershop.application.port.out.clock;

import java.time.Instant;

/**
 * 作用：抽象时间（便于测试与一致性）
 */
public interface ClockPort {
    Instant now();
}
