package com.surge.common.json.plugins.sensitive;

/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 *
 * @author lichunqing
 * @version 3.6.0
 */
public interface SensitiveService {

    /**
     * 是否脱敏
     */
    boolean isSensitive();

}
