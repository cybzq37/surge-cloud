package com.surge.system.service;


import com.surge.system.domain.bo.SysOssBo;
import com.surge.system.domain.vo.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 文件上传 服务层
 *
 * @author lichunqing
 */
public interface ISysFileService {

    String queryByIds(String fileIds);

    String queryByIds(List<Long> fileIds);

    SysOssVo upload(File file);

    SysOssVo upload(MultipartFile file);

    Boolean insertByBo(SysOssBo bo);

    void download(Long ossId, HttpServletResponse response) throws IOException;

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
