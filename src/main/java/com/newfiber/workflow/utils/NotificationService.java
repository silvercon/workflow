package com.newfiber.workflow.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 */
@Slf4j
@Service
public class NotificationService {

    @Resource
    private JavaMailSender javaMailSender;

    private String smsUrl = "http://localhost:8999/sms_send";

    @Value("${spring.mail.username}")
    private String emailSenderFrom;

    public boolean sendSimpleMail(String to, String subject, String content){
        if(StringUtils.isBlank(emailSenderFrom)){
            log.error("邮件发送失败：请完成SpringMail配置");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailSenderFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败：", e);
            return false;
        }
        return true;
    }

    public boolean sendTencentSms(String mobile, String smsSign, String smsTemplateCode, List<String> templateArgs){
        Map<String,Object> requestMap = new HashMap<>();
        Map<String,Object> requestParamMap = new HashMap<>();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String platform = "500";

        requestParamMap.put("channel", "tencent");
        requestParamMap.put("sendType", "1");
        requestParamMap.put("platform", platform);
        requestParamMap.put("receiveMobile", mobile);
        requestParamMap.put("signName", smsSign);
        requestParamMap.put("templateCode", smsTemplateCode);
        requestParamMap.put("timeStamp", timeStamp);
        requestParamMap.put("variable", templateArgs);

        requestMap.put("data", requestParamMap);
        String result = HttpUtil.createPost(smsUrl).header("content-type", "application/json;charset=UTF-8").
                body(JSONObject.toJSONString(requestMap)).execute().body();

        SmsResult smsResult = JSONObject.parseObject(result, SmsResult.class);

        if(null != smsResult && StringUtils.isNotBlank(smsResult.getData().getDescription())){
            log.error("短信发送失败：{}", smsResult.getData().getDescription());
        }

        return null == smsResult || StringUtils.isBlank(smsResult.getData().getDescription());
    }

}

@Data
class SmsResult{
    private String result;
    private String msg;
    private SmsResultData data;
}

@Data
class SmsResultData{
    private String code;
    private String bizId;
    private String description;
    private String result;
}