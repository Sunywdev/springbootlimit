package com.sunyw.xyz.service;

import com.sunyw.xyz.dao.LogInfoMapper;
import com.sunyw.xyz.domain.LogInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * 日志保存
 */
@Service
@Slf4j
public class RequestPathSave {

    @Autowired
    private LogInfoMapper logInfoMapper;


    @Async("threadConfig")
    public void saveRequestPath(long startTime, long endTime,String ip, String requestParam, String responseParam, String method, String traceId) {
        LogInfo logInfo = new LogInfo();
        logInfo.setRequestTime(startTime);
        logInfo.setRequestIp(ip);
        logInfo.setRequestMethod(method);
        logInfo.setRequestParam(requestParam);
        logInfo.setResponseParam(responseParam);
        logInfo.setResponseTime(endTime);
        logInfo.setTraceId(traceId);
        logInfo.setTimeConsuming(endTime-startTime);
        log.info("参数信息:{}",logInfo.toString());
        logInfoMapper.insertSelective(logInfo);
    }
}
