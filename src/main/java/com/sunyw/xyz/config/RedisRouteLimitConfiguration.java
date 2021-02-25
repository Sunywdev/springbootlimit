package com.sunyw.xyz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 限流配置类
 */
@Component
@ConfigurationProperties(prefix = "route")
@Data
public class RedisRouteLimitConfiguration {

    /**
     * 每秒产生的令牌数量
     */
    private String replenishrate = "1";

    /**
     * 桶内令牌容量
     */
    private String burstcapacity = "1";

    /**
     * 每次获取令牌数量
     */
    private String acquiredquantity = "1";

    /**
     * 发送人邮箱
     */
    private String formmailaddress;

    /**
     * 服务异常通知人邮箱,多个使用 , 分隔开
     */
    private String sendmailaddress;

    /**
     * 是否开启日志链路追踪
     */
    private Boolean startlog = false;


}
