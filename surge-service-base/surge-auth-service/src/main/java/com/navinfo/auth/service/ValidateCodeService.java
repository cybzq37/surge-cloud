package com.surge.auth.service;

import com.surge.common.core.exception.ServiceException;

import java.util.Map;

/**
 * 验证码处理
 *
 * @author lichunqing
 */
public interface ValidateCodeService {

    /**
     * 创建验证码
     */
    Map<String, Object> createCaptcha() throws ServiceException;

    /**
     * 校验验证码
     */
    void checkCaptcha(String key, String value) throws ServiceException;
}
