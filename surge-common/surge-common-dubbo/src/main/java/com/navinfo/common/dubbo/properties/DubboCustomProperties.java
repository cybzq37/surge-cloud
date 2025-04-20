package com.surge.common.dubbo.properties;

import com.surge.common.dubbo.enumd.RequestLogEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 自定义配置
 *
 * @author lichunqing
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "dubbo.custom")
public class DubboCustomProperties {

    private Boolean requestLog;

    private RequestLogEnum logLevel;

}
