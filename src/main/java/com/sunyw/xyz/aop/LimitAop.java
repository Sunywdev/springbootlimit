package com.sunyw.xyz.aop;

import com.sunyw.xyz.annotation.RouteLimiter;
import com.sunyw.xyz.config.RedisRouteLimitConfiguration;
import com.sunyw.xyz.enums.ErrorEnum;
import com.sunyw.xyz.exception.LimitException;
import com.sunyw.xyz.utils.EmaiUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;

/***
 *基于redis+lua实现分布式限流
 */
@Aspect
@Component
@Slf4j
public class LimitAop {
    /**
     * lua脚本
     */
    @Autowired
    private RedisScript<Boolean> redisScriptAop;

    /**
     * redis
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 邮件发送服务
     */
    @Autowired
    private EmaiUtil emaiUtil;

    /**
     * 配置项
     */
    @Autowired
    private RedisRouteLimitConfiguration redisLimitConfiguration;

    /**
     * 时间戳在redis中的key
     */
    private static final String LIMIT_TIME_KEY="LIMIT_TIME_KEY";


    @Pointcut(value="@annotation(com.sunyw.xyz.annotation.RouteLimiter)")
    public void cut() {
    }

    @Before(value="cut()")
    public void check(JoinPoint joinPoint) {
        log.info("[限流控制]>>>>>>>>>>>>>>>>>>>>>>>>>开始执行");
        Method method=((MethodSignature) joinPoint.getSignature()).getMethod();
        RouteLimiter routeLimiter=method.getAnnotation(RouteLimiter.class);
        if (ObjectUtils.isEmpty(routeLimiter)) {
            return;
        }
        if (routeLimiter.repleniShrate() > 0) {
            redisLimitConfiguration.setReplenishrate(String.valueOf(routeLimiter.repleniShrate()));
        }
        if (routeLimiter.burstCapacity() > 0) {
            redisLimitConfiguration.setBurstcapacity(String.valueOf(routeLimiter.burstCapacity()));
        }
        if (routeLimiter.acquiredQuantity() > 0) {
            redisLimitConfiguration.setAcquiredquantity(String.valueOf(routeLimiter.acquiredQuantity()));
        }
        log.info("[限流控制]>>>>>>>>>>>>>>>>>>>>>>>>>限流配置为:[{}]",redisLimitConfiguration.toString());
        HttpServletRequest request=getHttpServletRequest();
        String requestUrl=request.getRequestURL().toString();
        List<String> list=Arrays.asList(requestUrl,LIMIT_TIME_KEY);
        Boolean nowToken=redisTemplate.execute(redisScriptAop,list,redisLimitConfiguration.getReplenishrate(),redisLimitConfiguration.getBurstcapacity(),Instant.now().getEpochSecond() + "",redisLimitConfiguration.getAcquiredquantity());
        log.info("[限流控制]>>>>>>>>>>>>>>>>>>>>>>>>>获取令牌结果为: [{}]",nowToken);
        if (ObjectUtils.isEmpty(nowToken)) {
            log.error("------------------------剩余令牌为空,已拒绝请求,请查看redis服务是否正常,已通知系统管理员");
            if (!StringUtils.isEmpty(redisLimitConfiguration.getSendmailaddress())) {
                String[] sendAddr=redisLimitConfiguration.getSendmailaddress().split(",");
                emaiUtil.sendSimpleMail(sendAddr,"网关令牌获取为空预警","剩余令牌为空,已拒绝请求,请查看redis服务是否正常");
            } else {
                log.warn("[限流控制]>>>>>>>>>>>>>>>>>>>>>>>>>接收人邮箱地址为空,请查看配置信息");
            }
            throw new LimitException(ErrorEnum.SERVER_ERROR.getCode(),ErrorEnum.SERVER_ERROR.getDesc());
        }
        if (!nowToken) {
            log.info("[限流控制]>>>>>>>>>>>>>>>>>>>>>>>>>访问次数过快,已拒绝请求");
            throw new LimitException(ErrorEnum.TOO_FAST.getCode(),ErrorEnum.TOO_FAST.getDesc());
        }
        log.info("[限流控制]>>>>>>>>>>>>>>>>>>>>>>>>>限流拦截验证通过");
    }

    private static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

}
