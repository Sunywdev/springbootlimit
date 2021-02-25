package com.sunyw.xyz.aop;

import com.alibaba.fastjson.JSON;
import com.sunyw.xyz.annotation.Log;
import com.sunyw.xyz.config.RedisRouteLimitConfiguration;
import com.sunyw.xyz.exception.LimitException;
import com.sunyw.xyz.service.RequestPathSave;
import com.sunyw.xyz.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Log注解解释器
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class LogAsp {

    @Autowired
    private RequestPathSave requestPathSave;

    @Autowired
    private RedisRouteLimitConfiguration limitConfiguration;

    private static final String traceId = "traceId";

    /**
     * 切点
     */
    @Pointcut("@annotation(com.sunyw.xyz.annotation.Log)")
    public void pointCut() {

    }

    /**
     * 环绕日志打印
     *
     * @param joinPoint 连接点
     * @return Object
     * @throws Throwable 异常
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MDC.put(traceId, UUID.randomUUID().toString());
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Log annotation = method.getAnnotation(Log.class);
        if (null == annotation) {
            return joinPoint.getArgs();
        }
        String name = annotation.name();
        long startTime = System.currentTimeMillis();
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>方法[{}]开始执行,开始时间{}", name, startTime);
        HttpServletRequest request = getRequest();
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>请求方法路径为:{}", request.getRequestURL());
        StringBuilder params = new StringBuilder();
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                params.append(argNames[i]).append(":").append(argValues[i]);
            }
        }
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>请求参数为:[{}]", params + "");
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (LimitException e) {
            log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>开始进行记录");
            String stackTrace = stackTrace(e);
            log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>错误信息为:{}", stackTrace);
            if (limitConfiguration.getStartlog()) {
                requestPathSave.saveRequestPath(startTime, System.currentTimeMillis(), IpUtils.getIpAddr(request), params + "", e.getDesc(), method.getName(), MDC.get(traceId));
            }
            throw new LimitException(e.getCode(), e.getDesc());
        } catch (Exception e) {
            log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>开始进行记录");
            String stackTrace = stackTrace(e);
            log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>错误信息为:{}", stackTrace);
            if (limitConfiguration.getStartlog()) {
                requestPathSave.saveRequestPath(startTime, System.currentTimeMillis(), IpUtils.getIpAddr(request), params + "", e.getMessage(), method.getName(), MDC.get(traceId));
            }
            throw new Exception(e.getMessage());
        }
        Object Json;
        if (!ObjectUtils.isEmpty(proceed)) {
            Json = "";
        } else {
            Json = JSON.toJSON(proceed);
        }
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>响应参数为: [{}]", Json);
        long endTime = System.currentTimeMillis();
        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>执行完毕耗时 :{}", (endTime - startTime) + "ms");
        if (limitConfiguration.getStartlog()) {
            requestPathSave.saveRequestPath(startTime, endTime, IpUtils.getIpAddr(request), params + "", Json + "", method.getName(), MDC.get(traceId));
        }
        return proceed;
    }
    /**
     * 堆栈异常获取
     *
     * @param throwable 异常
     * @return String
     */
    private static String stackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    /**
     * 获取HttpServletRequest
     *
     * @return HttpServletRequest
     */
    private HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        return servletRequestAttributes.getRequest();
    }
}
