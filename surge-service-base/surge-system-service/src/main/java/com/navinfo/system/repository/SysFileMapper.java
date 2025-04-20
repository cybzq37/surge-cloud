package com.surge.system.repository;

import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.system.domain.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传 数据层
 *
 * @author lichunqing
 */
@Mapper
public interface SysFileMapper extends BaseMapperPlus<SysFileMapper, SysFile, SysFile> {
}
