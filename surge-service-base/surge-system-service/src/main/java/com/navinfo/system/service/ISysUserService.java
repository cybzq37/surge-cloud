package com.surge.system.service;


import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.system.domain.entity.SysUser;

/**
 * 用户 业务层
 *
 * @author lichunqing
 */
public interface ISysUserService {

    SysUser queryById(Long userId);

    SysUser queryByUsername(String username);

    PageInfo<SysUser> queryPage(String username,
                             String realname,
                             String phone,
                             String status,
                             Long sysOrgId,
                             PageInfo<SysUser> page);

    void createUser(SysUser user);

    void updateUser(SysUser user);

    void deleteUser(Long userId);

    void resetPassword(Long id, String password);

    boolean checkIfUsernameExist(String username);

    boolean checkIfPhoneExist(String phone);

    boolean checkIfEmailExist(String email);



}
