package com.surge.auth.service.impl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import com.surge.auth.captcha.enums.CaptchaType;
import com.surge.auth.config.properties.CaptchaProperties;
import com.surge.common.core.constant.redis.CacheConstants;
import com.surge.common.core.constant.Constants;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.result.ErrorGlobal;
import com.surge.common.core.utils.SpringUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.core.utils.reflect.ReflectUtils;
import com.surge.common.redis.utils.RedisUtils;
import com.surge.auth.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码实现处理
 *
 * @author lichunqing
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private CaptchaProperties captchaProperties;

    /**
     * 生成验证码
     */
    @Override
    public Map<String, Object> createCaptcha() throws ServiceException {
        Map<String, Object> result = new HashMap<>();
        boolean captchaEnabled = captchaProperties.getEnabled();
        result.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return result;
        }

        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }
        RedisUtils.set(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        result.put("uuid", uuid);
        result.put("img", captcha.getImageBase64());
        return result;
    }

    /**
     * 校验验证码
     */
    @Override
    public void checkCaptcha(String code, String uuid) throws ServiceException {
        boolean captchaEnabled = captchaProperties.getEnabled();
        if(!captchaEnabled) {
            return;
        }
        if (StringUtils.isEmpty(code)) {
            throw new ServiceException("user.jcaptcha.not.blank");
        }
        if (StringUtils.isEmpty(uuid)) {
            throw new ServiceException("验证码过期");
        }
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = RedisUtils.get(verifyKey);
        RedisUtils.delete(verifyKey);

        if (!code.equalsIgnoreCase(captcha)) {
            throw new ServiceException(ErrorGlobal.failed);
        }
    }
}
