package com.surge.system.controller;

import cn.dev33.satoken.secure.BCrypt;
import com.surge.common.core.result.R;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Update;
import com.surge.common.mybatis.pagination.PageInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.satoken.utils.LoginHelper;
import com.surge.system.domain.entity.SysUser;
import com.surge.system.domain.model.LoginUser;
import com.surge.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(name = "用户管理接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class SysUserController {

    private final ISysUserService sysUserService;

    @Operation(summary = "当前登录用户详情", description = "用户登录后, 获取登录用户信息")
    @GetMapping("/profile")
    public R<SysUser> getProfile() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        SysUser sysUser = sysUserService.queryById(loginUser.getUserId());
        return R.ok(sysUser);
    }

    @Operation(summary = "用户分页")
    @Parameters({
            @Parameter(name = "username", description = "登录账号", in = ParameterIn.QUERY),
            @Parameter(name = "realname", description = "真实名称", in = ParameterIn.QUERY),
            @Parameter(name = "phone",description = "手机号", in = ParameterIn.QUERY),
            @Parameter(name = "status",description = "帐号状态（0正常 1停用）", in = ParameterIn.QUERY),
            @Parameter(name = "sysOrgId",description = "组织机构Id", in = ParameterIn.QUERY),
            @Parameter(name = "current",description = "页码", in = ParameterIn.QUERY),
            @Parameter(name = "size",description = "分页大小", in = ParameterIn.QUERY)
    })
    @GetMapping("/page")
    public R<PageInfo<SysUser>> queryPage(String username,
                                    String realname,
                                    String phone,
                                    String status,
                                    Long sysOrgId,
                                    PageInfo<SysUser> page) {
        return R.ok(sysUserService.queryPage(username, realname, phone, status, sysOrgId, page));
    }

    @Operation(summary = "根据Id查用户详情")
    @Parameters({
            @Parameter(name = "userId", description = "用户Id", in = ParameterIn.PATH)
    })
    @GetMapping(value = {"/info/{userId}"})
    public R<SysUser> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        SysUser sysUser = sysUserService.queryById(userId);
        return R.ok(sysUser);
    }

    @Operation(summary = "用户创建",description = "用户创建")
    @JsonView(Create.class)
    @PostMapping
    public R createUser(@Validated @RequestBody SysUser sysUser) {
        if (sysUserService.checkIfUsernameExist(sysUser.getUsername())) {
            throw new ServiceException("用户名已存在");
        }
        if(sysUser.getOrgIds() == null || sysUser.getOrgIds().size() == 0) {
            throw new ServiceException("组织机构不能为空");
        }
        if(sysUser.getRoleIds() == null || sysUser.getRoleIds().size() == 0) {
            throw new ServiceException("角色不能为空");
        }
        sysUser.setPassword(BCrypt.hashpw("Aa@1234!"));
        sysUserService.createUser(sysUser);
        return R.ok();
    }

    @Operation(summary = "用户更新",description = "用户更新")
    @PutMapping
    @JsonView(Update.class)
    public R updateUser(@Validated @RequestBody SysUser sysUser) {
        SysUser oldSysUser = sysUserService.queryById(sysUser.getId());
        if(!oldSysUser.getUsername().equals(sysUser.getUsername())) {
            throw new ServiceException("用户名不能修改");
        }
        if(sysUser.getOrgIds() == null || sysUser.getOrgIds().size() == 0) {
            throw new ServiceException("组织机构不能为空");
        }
        if(sysUser.getRoleIds() == null || sysUser.getRoleIds().size() == 0) {
            throw new ServiceException("角色不能为空");
        }
        sysUserService.updateUser(sysUser);
        return R.ok();
    }

    @Operation(summary = "用户删除")
    @Parameters({
            @Parameter(name = "userId", description = "用户Id", in = ParameterIn.PATH)
    })
    @DeleteMapping("/{userId}")
    public R deleteUser(@PathVariable Long userId) {
        if(userId.equals(1L)) {
            throw new ServiceException("超级管理员不允许删除");
        }
        if(Objects.equals(userId, LoginHelper.getUserId())) {
            throw new ServiceException("当前用户为登录账户，不能删除");
        }
        sysUserService.deleteUser(userId);
        return R.ok();
    }

    @Operation(summary = "重置用户密码", description = "重置用户密码为默认密码")
    @Parameters({
            @Parameter(name = "userId", description = "用户Id", in = ParameterIn.PATH)
    })
    @PutMapping("/{userId}/resetpwd")
    public R resetPassword(@PathVariable Long userId) {
        sysUserService.resetPassword(userId, BCrypt.hashpw("Aa@1234!"));
        return R.ok();
    }

    @Operation(summary = "修改用户密码", description = "修改当前登录用户密码")
    @Parameters({
            @Parameter(name = "oldPassword", description = "旧密码", in = ParameterIn.DEFAULT),
            @Parameter(name = "newPassword", description = "新密码", in = ParameterIn.DEFAULT)
    })
    @PutMapping("/changepwd")
    public R changePassword(String oldPassword, String newPassword) {
        SysUser user = sysUserService.queryById(LoginHelper.getUserId());
        String password = user.getPassword();
        if (!BCrypt.checkpw(oldPassword, password)) {
            throw new ServiceException("修改密码失败，旧密码错误");
        }
        if (BCrypt.checkpw(newPassword, password)) {
            throw new ServiceException("新密码不能与旧密码相同");
        }
        sysUserService.resetPassword(user.getId(), BCrypt.hashpw(newPassword));
        return R.ok();
    }





    /**
     * 修改用户
     */
//    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public R<Void> updateProfile(@RequestBody SysUser user) {
//        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
//            return R.fail("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
//        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
//            return R.fail("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
//        }
//        user.setUserId(LoginHelper.getUserId());
//        user.setUserName(null);
//        user.setPassword(null);
//        user.setAvatar(null);
//        user.setDeptId(null);
//        if (userService.updateUserProfile(user) > 0) {
//            return R.ok();
//        }
//        return R.fail("修改个人信息异常，请联系管理员");
//    }




    /**
     * 头像上传
     *
     * @param avatarfile 用户头像
     */
//    @GlobalTransactional(rollbackFor = Exception.class)
//    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
//    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public R<Map<String, Object>> avatar(@RequestPart("avatarfile") MultipartFile avatarfile) throws IOException {
//        if (!avatarfile.isEmpty()) {
//            String extension = FileUtil.extName(avatarfile.getOriginalFilename());
//            if (!StringUtils.equalsAnyIgnoreCase(extension, MimeTypeUtils.IMAGE_EXTENSION)) {
//                return R.fail("文件格式不正确，请上传" + Arrays.toString(MimeTypeUtils.IMAGE_EXTENSION) + "格式");
//            }
//            SysFileBO sysFileBO = remoteFileService.upload(avatarfile.getName(), avatarfile.getOriginalFilename(), avatarfile.getContentType(), avatarfile.getBytes());
//            if (ObjectUtil.isNull(sysFileBO)) {
//                return R.fail("文件服务异常，请联系管理员");
//            }
//            String url = sysFileBO.getUrl();
//            if (userService.updateUserAvatar(LoginHelper.getUsername(), url)) {
//                Map<String, Object> ajax = new HashMap<>();
//                ajax.put("imgUrl", url);
//                return R.ok(ajax);
//            }
//        }
//        return R.fail("上传图片异常，请联系管理员");
//    }


    //    /**
//     * 在线用户列表
//     *
//     * @param ipaddr   ip地址
//     * @param userName 用户名
//     */
//    @SaCheckPermission("monitor:online:list")
//    @GetMapping("/list")
//    public PageInfo<SysUserOnline> list(String ipaddr, String userName) {
//        // 获取所有未过期的 token
//        List<String> keys = StpUtil.searchTokenValue("", 0, -1, false);
//        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
//        for (String key : keys) {
//            String token = StringUtils.substringAfterLast(key, ":");
//            // 如果已经过期则踢下线
//            if (StpUtil.stpLogic.getTokenActiveTimeoutByToken(token) < -1) {
//                continue;
//            }
//            userOnlineList.add(RedisUtils.getCacheObject(CacheConstants.ONLINE_TOKEN_KEY + token));
//        }
//        if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
//            userOnlineList = StreamUtils.filter(userOnlineList, userOnline ->
//                StringUtils.equals(ipaddr, userOnline.getIpaddr()) &&
//                    StringUtils.equals(userName, userOnline.getUserName())
//            );
//        } else if (StringUtils.isNotEmpty(ipaddr)) {
//            userOnlineList = StreamUtils.filter(userOnlineList, userOnline ->
//                StringUtils.equals(ipaddr, userOnline.getIpaddr())
//            );
//        } else if (StringUtils.isNotEmpty(userName)) {
//            userOnlineList = StreamUtils.filter(userOnlineList, userOnline ->
//                StringUtils.equals(userName, userOnline.getUserName())
//            );
//        }
//        Collections.reverse(userOnlineList);
//        userOnlineList.removeAll(Collections.singleton(null));
//        PageInfo<SysUserOnline> page = new Page<>();
//        page.setRecords(userOnlineList);
//        return page;
//    }
//
//    /**
//     * 强退用户
//     */
//    @SaCheckPermission("monitor:online:forceLogout")
//    @Log(title = "在线用户", businessType = BusinessType.FORCE)
//    @DeleteMapping("/{tokenId}")
//    public R<Void> forceLogout(@PathVariable String tokenId) {
//        try {
//            StpUtil.kickoutByTokenValue(tokenId);
//        } catch (NotLoginException ignored) {
//        }
//        return R.ok();
//    }
}
