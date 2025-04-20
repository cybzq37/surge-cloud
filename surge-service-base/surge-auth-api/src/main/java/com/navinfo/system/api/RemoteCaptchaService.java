package com.surge.system.api;

import com.surge.common.core.exception.ServiceException;

public interface RemoteCaptchaService {

    void checkCaptcha(String key, String value) throws ServiceException;
}
