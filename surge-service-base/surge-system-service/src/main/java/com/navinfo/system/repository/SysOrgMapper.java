package com.surge.system.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.system.domain.entity.SysOrg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author lichunqing
 */
public interface SysOrgMapper extends BaseMapperPlus<SysOrgMapper, SysOrg, SysOrg> {

    List<SysOrg> selectOrgListByUserId(@Param("userId") Long userId);

//    @DataPermission({
//        @DataColumn(key = "deptName", value = "dept_id")
//    })
    List<SysOrg> selectOrgList(@Param(Constants.WRAPPER) Wrapper<SysOrg> queryWrapper);

    PageInfo<SysOrg> selectOrgPage(@Param("page") PageInfo<SysOrg> page, @Param(Constants.WRAPPER) Wrapper<SysOrg> queryWrapper);

    List<SysOrg> selectOrgRecursion(@Param("orgId") Long orgId);
}
