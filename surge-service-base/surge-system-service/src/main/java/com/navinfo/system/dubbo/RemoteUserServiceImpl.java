package com.surge.system.dubbo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.surge.common.core.constant.enums.UserStatus;
import com.surge.system.domain.entity.SysRole;
import com.surge.system.error.ErrorSystem;
import com.surge.common.core.exception.ServiceException;
import com.surge.system.api.RemoteUserService;
import com.surge.system.domain.entity.SysUser;
import com.surge.system.domain.model.LoginUser;
import com.surge.system.domain.model.XcxLoginUser;
import com.surge.system.repository.SysRoleMapper;
import com.surge.system.repository.SysUserMapper;
//import com.surge.system.service.ISysPermissionService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务
 *
 * @author lichunqing
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteUserServiceImpl implements RemoteUserService {

//    private final ISysPermissionService permissionService;
    private final SysUserMapper userMapper;

    private final SysRoleMapper roleMapper;

    @Override
    public LoginUser getUserInfo(String username) throws ServiceException {
        SysUser sysUser = userMapper.selectByUsername(username);
        if (ObjectUtil.isNull(sysUser)) {
            throw new ServiceException(ErrorSystem.user_not_exist);
        }
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            throw new ServiceException(ErrorSystem.user_blocked);
        }
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        return buildLoginUser(sysUser);
    }



    @Override
    public XcxLoginUser getUserInfoByOpenid(String openid) throws ServiceException {
        // todo 自行实现 userService.selectUserByOpenid(openid);
        SysUser sysUser = new SysUser();
        if (ObjectUtil.isNull(sysUser)) {
            // todo 用户不存在 业务逻辑自行实现
        }
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            // todo 用户已被停用 业务逻辑自行实现
        }
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        XcxLoginUser loginUser = new XcxLoginUser();
        loginUser.setUserId(sysUser.getId());
        loginUser.setUsername(sysUser.getUsername());
        loginUser.setUserType(sysUser.getType());
        loginUser.setOpenid(openid);
        return loginUser;
    }

//    @Override
//    public Boolean registerUserInfo(SysUser sysUser) throws UserException, ServiceException {
//        String username = sysUser.getUsername();
//        if (!userService.checkIfUsernameExist(sysUser)) {
//            throw new UserException("user.register.save.error", username);
//        }
//        return userService.register(sysUser);
//    }

//    @Override
//    public String selectUserNameById(Long userId) {
//        return userService.selectUserNameById(userId);
//    }

    /**
     * 构建登录用户
     */
    private LoginUser buildLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setPassword(user.getPassword());
        loginUser.setUserType(user.getType());
        // 查询所有菜单权限
//        loginUser.setResources();
        // 查询所有角色权限
//        loginUser.setRoles(permissionService.getRolePermission(user));
        return loginUser;
    }

    public List<SysRole> getRoleByUser(String username){
        return roleMapper.selectRolesByUsername(username);
    }
}
