package com.surge.system.api;

import com.surge.common.core.exception.ServiceException;
import com.surge.system.domain.entity.SysRole;
import com.surge.system.domain.model.LoginUser;
import com.surge.system.domain.model.XcxLoginUser;

import java.util.List;

public interface RemoteUserService {

    LoginUser getUserInfo(String username) throws ServiceException;


    XcxLoginUser getUserInfoByOpenid(String openid) throws ServiceException;

    List<SysRole> getRoleByUser(String username) throws ServiceException;
}
