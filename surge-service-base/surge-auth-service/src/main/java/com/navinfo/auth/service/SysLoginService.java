package com.surge.auth.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.surge.auth.config.properties.UserPasswordProperties;
import com.surge.common.core.constant.redis.CacheConstants;
import com.surge.common.core.constant.Constants;
import com.surge.common.core.constant.enums.DeviceType;
import com.surge.common.core.constant.enums.LoginType;
import com.surge.common.core.exception.MessageUtils;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.ServletUtils;
import com.surge.common.core.utils.SpringUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.core.utils.ip.AddressUtils;
import com.surge.common.log.event.LogininforEvent;
import com.surge.common.redis.utils.RedisUtils;
import com.surge.common.satoken.utils.LoginHelper;
import com.surge.system.api.RemoteUserService;
import com.surge.system.domain.model.LoginUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.function.Supplier;

/**
 * 登录校验方法
 *
 * @author lichunqing
 */
@Service
public class SysLoginService {

    @DubboReference
    private RemoteUserService remoteUserService;
    @Autowired
    private UserPasswordProperties userPasswordProperties;

    public String login(String username, String password) {
        LoginUser loginUser = remoteUserService.getUserInfo(username);

        checkLogin(LoginType.PASSWORD, username, () -> !BCrypt.checkpw(password, loginUser.getPassword()));
        // 获取登录token
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);

        recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        return StpUtil.getTokenValue();
    }

    public void logout() {
        try {
            LoginUser loginUser = LoginHelper.getLoginUser();
            recordLogininfor(loginUser.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
        } catch (NotLoginException ignored) {
        } finally {
            try {
                StpUtil.logout();
            } catch (NotLoginException ignored) {
            }
        }
    }


    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    public void recordLogininfor(String username, String status, String message) {
        HttpServletRequest request = ServletUtils.getRequest();
        final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        final String ip = ServletUtils.getClientIP(request);

        String address = AddressUtils.getRealAddressByIP(ip);
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        LogininforEvent logininfor = new LogininforEvent();
        logininfor.setUserName(username);
        logininfor.setIpaddr(ip);
        logininfor.setLoginLocation(address);
        logininfor.setBrowser(browser);
        logininfor.setOs(os);
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        SpringUtils.context().publishEvent(logininfor);
    }

//    /**
//     * 校验短信验证码
//     */
//    private boolean validateSmsCode(String phonenumber, String smsCode) {
//        String code = RedisUtils.getCacheObject(CacheConstants.CAPTCHA_CODE_KEY + phonenumber);
//        if (StringUtils.isBlank(code)) {
//            recordLogininfor(phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
//            throw new CaptchaExpireException();
//        }
//        return code.equals(smsCode);
//    }
//
//    /**
//     * 校验邮箱验证码
//     */
//    private boolean validateEmailCode(String email, String emailCode) {
//        String code = RedisUtils.getCacheObject(CacheConstants.CAPTCHA_CODE_KEY + email);
//        if (StringUtils.isBlank(code)) {
//            recordLogininfor(email, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
//            throw new CaptchaExpireException();
//        }
//        return code.equals(emailCode);
//    }

    /**
     * 登录校验
     */
    private void checkLogin(LoginType loginType, String username, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;
        Integer maxRetryCount = userPasswordProperties.getMaxRetryCount();
        Integer lockTime = userPasswordProperties.getLockTime();

        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        Integer errorNumber = RedisUtils.get(errorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            recordLogininfor(username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
            throw new ServiceException("锁定时间内登录 则踢出");
        }

        if (supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.set(errorKey, errorNumber, Duration.ofMinutes(lockTime));
                recordLogininfor(username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
                throw new ServiceException("达到规定错误次数 则锁定登录");
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.set(errorKey, errorNumber);
                recordLogininfor(username, loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber));
                throw new ServiceException("用户名或密码错误");
            }
        }
        // 登录成功 清空错误次数
        RedisUtils.delete(errorKey);
    }
}
