package com.backend.flowershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * FlowerShop 后端启动入口（冻结文件）

 * 职责边界：
 * 1) 仅负责启动 Spring Boot
 * 2) 不允许写任何业务逻辑
 * 3) 不允许在这里做初始化数据、校验、权限判断等

 * 输入：无
 * 输出：启动 Web 服务（默认 8080）
 */

@SpringBootApplication
public class FlowerShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowerShopApplication.class, args);
	}

}
