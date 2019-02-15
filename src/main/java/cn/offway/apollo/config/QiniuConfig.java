package cn.offway.apollo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.offway.apollo.properties.QiniuProperties;


@Configuration
@EnableConfigurationProperties(QiniuProperties.class)
public class QiniuConfig {

}
