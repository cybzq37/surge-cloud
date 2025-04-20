package com.surge.system.api;

import com.surge.common.core.exception.ServiceException;
import com.surge.system.domain.bo.SysFileBO;

/**
 * 文件服务
 *
 * @author lichunqing
 */
public interface RemoteFileService {

    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    SysFileBO upload(String name, String originalFilename, String contentType, byte[] file) throws ServiceException;

    /**
     * 通过ossId查询对应的url
     *
     * @param ossIds ossId串逗号分隔
     * @return url串逗号分隔
     */
    String selectUrlByIds(String ossIds);

}
