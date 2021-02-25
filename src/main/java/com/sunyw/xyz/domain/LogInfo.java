package com.sunyw.xyz.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 链路日志保存DTO
 */
@Data
@ToString
public class LogInfo {

    private Integer id;

    /**
     * 链路追踪id
     */
    private String traceId;

    /**
     * 请求IP地址
     */
    private String requestIp;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求方法名称
     */
    private String requestMethod;

    /**
     * 请求开始时间
     */
    private long requestTime;

    /**
     * 响应参数
     */
    private String responseParam;

    /**
     * 结束时间
     */
    private long responseTime;

    /**
     * 耗时
     */
    private long timeConsuming;
}
