//package com.surge.system.dubbo;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.convert.Convert;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.surge.common.core.utils.StreamUtils;
//import com.surge.common.mybatis.helper.DataBaseHelper;
//import com.surge.system.api.RemoteDataScopeService;
//import com.surge.system.domain.entity.SysOrg;
//import com.surge.system.mapper.SysOrgMapper;
//import lombok.RequiredArgsConstructor;
//import org.apache.dubbo.config.annotation.DubboService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * 数据权限 实现
// * <p>
// * 注意: 此Service内不允许调用标注`数据权限`注解的方法
// * 例如: deptMapper.selectList 此 selectList 方法标注了`数据权限`注解 会出现循环解析的问题
// *
// * @author lichunqing
// */
//@RequiredArgsConstructor
//@Service
//@DubboService
//public class RemoteDataScopeServiceImpl implements RemoteDataScopeService {
//
//    private final SysRoleDeptMapper roleDeptMapper;
//    private final SysOrgMapper deptMapper;
//
//    @Override
//    public String getRoleCustom(Long roleId) {
//        List<SysRoleDept> list = roleDeptMapper.selectList(
//            new LambdaQueryWrapper<SysRoleDept>()
//                .select(SysRoleDept::getDeptId)
//                .eq(SysRoleDept::getRoleId, roleId));
//        if (CollUtil.isNotEmpty(list)) {
//            return StreamUtils.join(list, rd -> Convert.toStr(rd.getDeptId()));
//        }
//        return null;
//    }
//
//    @Override
//    public String getDeptAndChild(Long deptId) {
//        List<SysOrg> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysOrg>()
//            .select(SysOrg::getDeptId)
//            .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
//        List<Long> ids = StreamUtils.toList(deptList, SysOrg::getDeptId);
//        ids.add(deptId);
//        if (CollUtil.isNotEmpty(ids)) {
//            return StreamUtils.join(ids, Convert::toStr);
//        }
//        return null;
//    }
//
//}
