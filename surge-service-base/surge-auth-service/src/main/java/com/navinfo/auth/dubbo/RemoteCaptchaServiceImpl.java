package com.surge.auth.dubbo;

import com.surge.auth.service.ValidateCodeService;
import com.surge.common.core.exception.ServiceException;
import com.surge.system.api.RemoteCaptchaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteCaptchaServiceImpl implements RemoteCaptchaService {

    private final ValidateCodeService validateCodeService;

    /**
     * 校验验证码
     */
    @Override
    public void checkCaptcha(String key, String value) throws ServiceException {
        validateCodeService.checkCaptcha(key, value);
    }

}
