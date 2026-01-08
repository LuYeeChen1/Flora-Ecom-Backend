package com.backend.flowershop.infrastructure.clock;

import com.backend.flowershop.application.port.out.clock.ClockPort;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * 作用：系统时间适配器
 */
@Component
public class SystemClockAdapter implements ClockPort {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
