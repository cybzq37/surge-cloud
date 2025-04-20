package com.surge.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.system.domain.entity.SysOrg;

import java.util.List;

public interface ISysOrgService {

    SysOrg queryById(Long deptId);

    List<SysOrg> queryByUserId(Long userId);

    List<SysOrg> queryList(Long pid, String name);

    PageInfo<SysOrg> queryPage(Long pid, String name, PageInfo<SysOrg> page);

    List<Tree<Long>> queryTree(Long pid, String name);

    void createOrg(SysOrg sysOrg);

    void updateOrg(SysOrg sys);

    void deleteOrg(Long orgId);

    boolean checkIfHasUser(Long orgId);

    boolean hasChildNode(Long orgId) ;

    long queryAllChildNodes(Long orgId);

}
