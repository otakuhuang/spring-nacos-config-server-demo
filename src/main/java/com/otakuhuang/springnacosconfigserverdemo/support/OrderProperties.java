package com.otakuhuang.springnacosconfigserverdemo.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author otaku
 * @version 1.0
 * @date 2022/3/1 0:23
 * @description description
 */
@ConfigurationProperties("order")
@RefreshScope
@Data
@Component
public class OrderProperties {
    private Integer discount = 100;
    private String waiterPrefix = "springbucks-";
}
