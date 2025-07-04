package com.surge.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import com.surge.common.core.result.R;
import com.surge.common.log.annotation.Log;
import com.surge.common.log.enums.BusinessType;

import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.system.domain.entity.SysFile;
import com.surge.system.domain.vo.SysOssVo;
import com.surge.system.repository.SysFileRepository;
import com.surge.system.service.ISysFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传 控制层
 *
 * @author lichunqing
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class SysFileController {

    private final ISysFileService iSysFileService;
    private final SysFileRepository sysFileRepository;

    @GetMapping("/list")
    public R  list() {
        List<SysFile> list = sysFileRepository.list();
        return R.ok(list);
    }
//
//    /**
//     * 查询OSS对象基于id串
//     *
//     * @param ossIds OSS对象ID串
//     */
//    @SaCheckPermission("system:oss:list")
//    @GetMapping("/listByIds/{ossIds}")
//    public R<List<SysOssVo>> listByIds(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ossIds) {
//        List<SysOssVo> list = iSysFileService.listByIds(Arrays.asList(ossIds));
//        return R.ok(list);
//    }

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     */
    @SaCheckPermission("system:oss:upload")
    @Log(title = "OSS对象存储", businessType = BusinessType.INSERT)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            return R.fail("上传文件不能为空");
        }
        SysOssVo oss = iSysFileService.upload(file);
        Map<String, String> map = new HashMap<>(2);
        map.put("url", oss.getUrl());
        map.put("fileName", oss.getOriginalName());
        map.put("ossId", oss.getOssId().toString());
        return R.ok(map);
    }

    /**
     * 下载OSS对象存储
     *
     * @param ossId OSS对象ID
     */
    @SaCheckPermission("system:oss:download")
    @GetMapping("/download/{ossId}")
    public void download(@PathVariable Long ossId, HttpServletResponse response) throws IOException {
        iSysFileService.download(ossId, response);
    }

    /**
     * 删除OSS对象存储
     *
     * @param ossIds OSS对象ID串
     */
    @SaCheckPermission("system:oss:remove")
    @Log(title = "OSS对象存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ossIds) {
        iSysFileService.deleteWithValidByIds(Arrays.asList(ossIds), true);
        return R.ok();
    }

}
