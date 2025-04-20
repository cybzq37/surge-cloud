package com.surge.auth.form;

import com.surge.common.core.constant.UserConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 *
 * @author lichunqing
 */
@Data
@NoArgsConstructor
public class LoginBody {

    /**
     * 用户名
     */
    @Schema(description = "用户名", type = "String")
    @NotBlank(message = "用户名不能为空")
    @Length(min = UserConstant.USERNAME_MIN_LENGTH, max = UserConstant.USERNAME_MAX_LENGTH, message = "{user.username.length.valid}")
    private String username;

    /**
     * 用户密码
     */
    @Schema(description = "密码", type = "String")
    @NotBlank(message = "密码不能为空")
    @Length(min = UserConstant.PASSWORD_MIN_LENGTH, max = UserConstant.PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    private String password;

}
