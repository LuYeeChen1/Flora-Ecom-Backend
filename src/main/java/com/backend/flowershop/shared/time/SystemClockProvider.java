package com.backend.flowershop.shared.time;

import java.time.Instant;

/**
 * SystemClockProvider：基于系统时间的 ClockProvider 实现（冻结文件）

 * 职责边界：
 * 1) 唯一职责：返回系统当前时间
 * 2) 不包含任何业务逻辑
 * 3) 作为默认实现，可在测试中被替换

 * 注意：
 * - 业务代码只依赖 ClockProvider 接口
 * - 不应在业务代码中直接 new 本类
 */
public class SystemClockProvider implements ClockProvider {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
