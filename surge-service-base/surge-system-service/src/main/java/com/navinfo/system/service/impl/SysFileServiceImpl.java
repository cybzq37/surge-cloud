package com.surge.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.SpringUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.core.utils.file.FileUtils;

import com.surge.common.oss.core.OssClient;
import com.surge.common.oss.entity.UploadResult;
import com.surge.common.oss.enumd.AccessPolicyType;
import com.surge.common.oss.factory.OssFactory;
import com.surge.system.domain.entity.SysFile;
import com.surge.system.domain.bo.SysOssBo;
import com.surge.system.domain.vo.SysOssVo;
import com.surge.system.repository.SysFileMapper;
import com.surge.system.service.ISysFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SysFileServiceImpl implements ISysFileService {

    private final SysFileMapper sysFileMapper;

    @Override
    public String queryByIds(String fileIds) {
        List<String> list = new ArrayList<>();
        for (Long id : StringUtils.splitTo(fileIds, Convert::toLong)) {
            SysOssVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                try {
                    list.add(this.matchingUrl(vo).getUrl());
                } catch (Exception ignored) {
                    // 如果oss异常无法连接则将数据直接返回
                    list.add(vo.getUrl());
                }
            }
        }
        return String.join(StringUtils.SEPARATOR, list);
    }

    @Override
    public String queryByIds(List<Long> fileIds) {
        return "";
    }

    public SysOssVo getById(Long ossId) {
//        return sysFileMapper.selectVoById(ossId);
        return null;
    }

    @Override
    public void download(Long ossId, HttpServletResponse response) throws IOException {
        SysOssVo sysOss = SpringUtils.getAopProxy(this).getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        FileUtils.setAttachmentResponseHeader(response, sysOss.getOriginalName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        OssClient storage = OssFactory.instance(sysOss.getService());
        try(InputStream inputStream = storage.getObjectContent(sysOss.getUrl())) {
            int available = inputStream.available();
            IoUtil.copy(inputStream, response.getOutputStream(), available);
            response.setContentLength(available);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public SysOssVo upload(MultipartFile file) {
        String originalfileName = file.getOriginalFilename();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult;
        try {
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult);
    }

    @Override
    public SysOssVo upload(File file) {
        String originalfileName = file.getName();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult = storage.uploadSuffix(file, suffix);
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult);
    }

    private SysOssVo buildResultEntity(String originalfileName, String suffix, String configKey, UploadResult uploadResult) {
        SysFile oss = new SysFile();
        oss.setFilePath(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalfileName);
        sysFileMapper.insert(oss);
        SysOssVo sysOssVo = BeanUtil.toBean(oss, SysOssVo.class);
        return this.matchingUrl(sysOssVo);
    }

    @Override
    public Boolean insertByBo(SysOssBo bo) {
        SysFile oss = BeanUtil.toBean(bo, SysFile.class);
        boolean flag = sysFileMapper.insert(oss) > 0;
        if (flag) {
            bo.setOssId(oss.getId());
        }
        return flag;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<SysFile> list = sysFileMapper.selectBatchIds(ids);
        for (SysFile sysFile : list) {
//            OssClient storage = OssFactory.instance(sysFile.getService());
//            storage.delete(sysFile.getFilePath());
        }
        return sysFileMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 匹配Url
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private SysOssVo matchingUrl(SysOssVo oss) {
        OssClient storage = OssFactory.instance(oss.getService());
        // 仅修改桶类型为 private 的URL，临时URL时长为120s
        if (AccessPolicyType.PRIVATE == storage.getAccessPolicy()) {
            oss.setUrl(storage.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;
    }

}
