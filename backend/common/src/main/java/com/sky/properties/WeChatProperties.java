package com.sky.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.wechat")
@Data
public class WeChatProperties {

    private String appid; // Mini program AppID
    private String secret; // Mini program secret key
    private String mchid; // Merchant ID
    private String mchSerialNo; // Merchant API certificate serial number
    private String privateKeyFilePath; // Merchant private key file
    private String apiV3Key; // Certificate decryption key
    private String weChatPayCertFilePath; // Platform certificate
    private String notifyUrl; // Payment success callback URL
    private String refundNotifyUrl; // Refund success callback URL

}
