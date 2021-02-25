package com.sunyw.xyz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * redis分布式漏桶限流配置
 */
@Configuration
public class RedisLimitConfig {

    @Bean(name = "redisScriptAop")
    public RedisScript<Boolean> redisScriptAop() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<> ();
        redisScript.setScriptSource (new ResourceScriptSource (new ClassPathResource ("lua/limit.lua")));
        redisScript.setResultType (Boolean.class);
        return redisScript;
    }

    @Bean("redisScriptAsp")
    public RedisScript<Boolean> redisScriptAsp() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<> ();
        redisScript.setLocation (new ClassPathResource ("lua/rate.lua"));
        redisScript.setResultType (Boolean.class);
        return redisScript;
    }
}
