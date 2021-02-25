package com.sunyw.xyz.utils;

import com.sunyw.xyz.config.RedisRouteLimitConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
@Slf4j
public class EmaiUtil {

    /**
     * jms邮件服务
     */
    @Autowired
    private JavaMailSender jms;

    /**
     * 配置项
     */
    @Autowired
    private RedisRouteLimitConfiguration redisLimitConfiguration;

    /**
     * 简单文本邮件
     *
     * @param toAddress 收件人
     * @param title     主题
     * @param content   内容
     */
    @Async("threadConfig")
    public void sendSimpleMail(String[] toAddress, String title, String content) {
        if (StringUtils.isEmpty(redisLimitConfiguration.getFormmailaddress())) {
            log.error("邮件发送人信息未配置");
            return;
        }
        try {
            //建立邮件消息
            SimpleMailMessage mainMessage = new SimpleMailMessage();
            //发送者
            mainMessage.setFrom(redisLimitConfiguration.getFormmailaddress());
            //接收者
            mainMessage.setTo(toAddress);
            //发送的标题
            mainMessage.setSubject(title);
            //发送的内容
            mainMessage.setText(content);
            jms.send(mainMessage);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
        }
        log.info("发送邮件成功");
    }
}
