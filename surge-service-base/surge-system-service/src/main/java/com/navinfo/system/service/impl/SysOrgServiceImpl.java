package com.surge.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.common.core.constant.UserConstant;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.core.utils.tree.TreeBuildUtils;
import com.surge.common.mybatis.helper.DataBaseHelper;
import com.surge.system.domain.entity.SysOrg;
import com.surge.system.domain.entity.SysUser;
import com.surge.system.repository.SysOrgMapper;
import com.surge.system.repository.SysRoleMapper;
import com.surge.system.repository.SysUserMapper;
import com.surge.system.service.ISysOrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SysOrgServiceImpl implements ISysOrgService {

    private final SysOrgMapper sysOrgMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserMapper sysUserMapper;

    @Override
    public SysOrg queryById(Long id) {
        return sysOrgMapper.selectById(id);
    }

    @Override
    public List<SysOrg> queryByUserId(Long userId) {
        return sysOrgMapper.selectOrgListByUserId(userId);
    }

    @Override
    public List<SysOrg> queryList(Long pid, String name) {
        LambdaQueryWrapper<SysOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysOrg::getDelFlag, "0")
            .eq(ObjectUtil.isNotNull(pid), SysOrg::getPid, pid)
            .like(StringUtils.isNotBlank(name), SysOrg::getName, name)
            .orderByAsc(SysOrg::getSort);
        return sysOrgMapper.selectOrgList(wrapper);
    }

    @Override
    public PageInfo<SysOrg> queryPage(Long pid, String name, PageInfo<SysOrg> page) {
        LambdaQueryWrapper<SysOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysOrg::getDelFlag, "0")
                .eq(ObjectUtil.isNotNull(pid), SysOrg::getPid, pid)
                .like(StringUtils.isNotBlank(name), SysOrg::getName, name)
                .orderByAsc(SysOrg::getSort);
        return sysOrgMapper.selectOrgPage(page, wrapper);
    }

    @Override
    public List<Tree<Long>> queryTree(Long pid, String name) {
        List<SysOrg> orgs = this.queryList(pid, name);
        if (CollUtil.isEmpty(orgs)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(orgs, (org, tree) ->
                tree.setId(org.getId())
                        .setParentId(org.getPid())
                        .setName(org.getName())
                        .setWeight(org.getSort()));
    }


    @Override
    public long queryAllChildNodes(Long orgId) {
        return sysOrgMapper.selectCount(new LambdaQueryWrapper<SysOrg>()
            .eq(SysOrg::getStatus, UserConstant.DEPT_NORMAL)
            .apply(DataBaseHelper.findInSet(orgId, "ancestors")));
    }

    @Override
    public boolean hasChildNode(Long orgId) {
        return sysOrgMapper.exists(new LambdaQueryWrapper<SysOrg>()
            .eq(SysOrg::getPid, orgId));
    }

    @Override
    public boolean checkIfHasUser(Long orgId) {
        return sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getId, orgId));
    }

    @Override
    public void createOrg(SysOrg sysOrg) {
        SysOrg info = sysOrgMapper.selectById(sysOrg.getPid());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstant.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        sysOrg.setAncestors(info.getAncestors() + StringUtils.SEPARATOR + sysOrg.getPid());
        sysOrgMapper.insert(sysOrg);
    }

    @Override
    public void updateOrg(SysOrg sys) {
        SysOrg newParentOrg = sysOrgMapper.selectById(sys.getPid());
        SysOrg oldOrg = sysOrgMapper.selectById(sys.getId());
        if (ObjectUtil.isNotNull(newParentOrg) && ObjectUtil.isNotNull(oldOrg)) {
            String newAncestors = newParentOrg.getAncestors() + StringUtils.SEPARATOR + newParentOrg.getId();
            String oldAncestors = oldOrg.getAncestors();
            sys.setAncestors(newAncestors);
            updateChildOrg(sys.getId(), newAncestors, oldAncestors);
        }
        sysOrgMapper.updateById(sys);
        if (UserConstant.DEPT_NORMAL.equals(sys.getStatus()) && StringUtils.isNotEmpty(sys.getAncestors())
            && !StringUtils.equals(UserConstant.DEPT_NORMAL, sys.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentOrg(sys);
        }
    }

    private void updateChildOrg(Long deptId, String newAncestors, String oldAncestors) {
        List<SysOrg> children = sysOrgMapper.selectList(new LambdaQueryWrapper<SysOrg>()
                .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
        List<SysOrg> list = new ArrayList<>();
        for (SysOrg child : children) {
            SysOrg sysOrg = new SysOrg();
            sysOrg.setId(child.getId());
            sysOrg.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            list.add(sysOrg);
        }
        if (list.size() > 0) {
            sysOrgMapper.updateBatchById(list);
        }
    }

    private void updateParentOrg(SysOrg sysOrg) {
        String ancestors = sysOrg.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        sysOrgMapper.update(null, new LambdaUpdateWrapper<SysOrg>()
            .set(SysOrg::getStatus, UserConstant.DEPT_NORMAL)
            .in(SysOrg::getId, Arrays.asList(deptIds)));
    }



    @Override
    public void deleteOrg(Long orgId) {
        sysOrgMapper.deleteById(orgId);
    }

}
