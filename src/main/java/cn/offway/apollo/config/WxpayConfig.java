package cn.offway.apollo.config;

import cn.offway.apollo.properties.WxpayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WxpayProperties.class)
public class WxpayConfig {
}
