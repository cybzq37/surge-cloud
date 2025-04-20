package com.surge.map.domain.dto;

import com.surge.map.domain.entity.DataEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.InputStream;
import java.util.List;

@Data
public class DataEntryUploadFileDTO {

    @Schema(description = "数据名称")
    private String name;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件路径")
    private List<String> url;

    @Schema(description = "csv字段名")
    private String fieldName;

    @Schema(description = "epsg")
    private Integer epsg;

    @Schema(description = "坐标系")
    private String coordinateSystem;

    /**
     * 路径
     */
    private String path;
    /**
     * 内容类型
     */
    private String contentType;
    /**
     * 文件流
     */
    private InputStream inputStream;
    /**
     * 文件后缀
     */
    private String fileSuffix;
    /**
     * 文件MD5
     */
    private String fileMD5;
    /**
     * 文件大小
     */
    private long size;

    // 上传到服务的文件路径
    private String localFilePath;

    // 上传到服务器解压后的文件目录
    private String localUnzipFileDir;

    private DataEntry dataEntry;


}
