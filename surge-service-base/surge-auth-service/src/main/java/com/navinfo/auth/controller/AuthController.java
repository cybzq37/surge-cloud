package com.surge.auth.controller;

import com.surge.auth.form.LoginBody;
import com.surge.auth.service.SysLoginService;
import com.surge.auth.service.ValidateCodeService;
import com.surge.common.core.constant.Constants;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.result.R;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Validated
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final ValidateCodeService validateCodeService;
    private final SysLoginService sysLoginService;

    @PostMapping("code")
    public R<Map> code() {
        Map<String, Object> result = null;
        try {
            result = validateCodeService.createCaptcha();
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
        return R.ok(result);
    }

    @Operation(summary = "登录接口")
    @PostMapping("login")
    public R<Map> login(@Validated @RequestBody LoginBody form) {
        // 用户登录
        String accessToken = sysLoginService.login(form.getUsername(), form.getPassword());
        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put(Constants.ACCESS_TOKEN, accessToken);
        return R.ok(rspMap);
    }

    /**
     * 短信登录
     *
     * @param smsLoginBody 登录信息
     * @return 结果
     */
//    @PostMapping("/smsLogin")
//    public R<Map<String, Object>> smsLogin(@Validated @RequestBody SmsLoginBody smsLoginBody) {
//        Map<String, Object> ajax = new HashMap<>();
//        // 生成令牌
//        String token = sysLoginService.smsLogin(smsLoginBody.getPhonenumber(), smsLoginBody.getSmsCode());
//        ajax.put(Constants.ACCESS_TOKEN, token);
//        return R.ok(ajax);
//    }

    /**
     * 邮件登录
     *
     * @param body 登录信息
     * @return 结果
     */
//    @PostMapping("/emailLogin")
//    public R<Map<String, Object>> emailLogin(@Validated @RequestBody EmailLoginBody body) {
//        Map<String, Object> ajax = new HashMap<>();
//        // 生成令牌
//        String token = sysLoginService.emailLogin(body.getEmail(), body.getEmailCode());
//        ajax.put(Constants.ACCESS_TOKEN, token);
//        return R.ok(ajax);
//    }

    /**
     * 小程序登录(示例)
     *
     * @param xcxCode 小程序code
     * @return 结果
     */
//    @PostMapping("/xcxLogin")
//    public R<Map<String, Object>> xcxLogin(@NotBlank(message = "{xcx.code.not.blank}") String xcxCode) {
//        Map<String, Object> ajax = new HashMap<>();
//        // 生成令牌
//        String token = sysLoginService.xcxLogin(xcxCode);
//        ajax.put(Constants.ACCESS_TOKEN, token);
//        return R.ok(ajax);
//    }

    /**
     * 登出方法
     */
    @Operation(summary = "退出接口")
    @GetMapping("logout")
    public R logout() {
        sysLoginService.logout();
        return R.ok();
    }

    /**
     * 用户注册
     */
//    @PostMapping("register")
//    public R<Void> register(@RequestBody RegisterBody registerBody) {
//        // 用户注册
//        sysLoginService.register(registerBody);
//        return R.ok();
//    }

}
