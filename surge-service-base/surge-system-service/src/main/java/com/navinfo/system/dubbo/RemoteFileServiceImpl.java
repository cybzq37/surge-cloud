package com.surge.system.dubbo;

import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.oss.core.OssClient;
import com.surge.common.oss.entity.UploadResult;
import com.surge.common.oss.factory.OssFactory;
import com.surge.system.api.RemoteFileService;
import com.surge.system.domain.bo.SysFileBO;
import com.surge.system.domain.bo.SysOssBo;
import com.surge.system.service.ISysFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件请求处理
 *
 * @author lichunqing
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DubboService
public class RemoteFileServiceImpl implements RemoteFileService {

    private final ISysFileService sysOssService;

    /**
     * 文件上传请求
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysFileBO upload(String name, String originalFilename, String contentType, byte[] file) throws ServiceException {
        try {
            String suffix = StringUtils.substring(originalFilename, originalFilename.lastIndexOf("."), originalFilename.length());
            OssClient storage = OssFactory.instance();
            UploadResult uploadResult = storage.uploadSuffix(file, suffix, contentType);
            // 保存文件信息
            SysOssBo oss = new SysOssBo();
            oss.setUrl(uploadResult.getUrl());
            oss.setFileSuffix(suffix);
            oss.setFileName(uploadResult.getFilename());
            oss.setOriginalName(originalFilename);
            oss.setService(storage.getConfigKey());
            sysOssService.insertByBo(oss);
            SysFileBO sysFileBO = new SysFileBO();
            sysFileBO.setOssId(oss.getOssId());
            sysFileBO.setName(uploadResult.getFilename());
            sysFileBO.setUrl(uploadResult.getUrl());
            return sysFileBO;
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new ServiceException("上传文件失败");
        }
    }

    /**
     * 通过ossId查询对应的url
     *
     * @param ossIds ossId串逗号分隔
     * @return url串逗号分隔
     */
    @Override
    public String selectUrlByIds(String ossIds) {
        return sysOssService.queryByIds(ossIds);
    }

}
