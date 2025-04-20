package com.surge.common.json.plugins.xss;

import cn.hutool.core.util.ReUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义xss校验注解实现
 *
 * @author lichunqing
 */
public class XssValidator implements ConstraintValidator<Xss, String> {

    public final static String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !ReUtil.contains(RE_HTML_MARK, value);
    }

}
