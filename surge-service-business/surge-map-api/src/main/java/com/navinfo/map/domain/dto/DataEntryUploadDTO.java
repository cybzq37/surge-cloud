package com.surge.map.domain.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.json.view.Create;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DataEntryUploadDTO implements Serializable {
    private static final long serialVersionUID = -1657080927110990168L;

    @JsonView({Create.class})
    @Schema(description = "数据类型", required = true)
    private Integer dataType;

    @JsonView({Create.class})
    @Schema(description = "文件类型", required = true)
    private Integer fileType;

    @JsonView({Create.class})
    @Schema(description = "文件上传: [{\"name\":\"数据名称\",\"fileName\":\"文件名称\",\"url\":[\"文件路径1\",\"文件路径2\"],\"zipName\":\"压缩包名称\",\"fieldName\":\"csv字段名\",\"coordinateSystem\":\"EPSG:4326 - WGS84\"}]", required = true)
    public String uploadFile;

    @JsonView({Create.class})
    @Schema(description = "数据库上传：[{\"name\":\"数据名称\",\"tableName\":\"数据库表名\",\"coordinateSystem\":\"EPSG:4326 - WGS84\"]")
    public String uploadDB;

    @JsonView({Create.class})
    @Schema(description = "上传文件", required = true)
    private MultipartFile[] files;

    /**
     * 数据源类型 1:数据库  2:文件
     */
    @JsonView({Create.class})
    @NotNull(message = "数据源类型不能为空")
    @Schema(description = "数据源类型 1:数据库  2:文件 ", required = true)
    private Integer sourceType;

    @JsonView({Create.class})
    @Schema(description = "数据源id", required = true)
    private Long sourceId;

    @JsonView({Create.class})
    @Schema(description = "描述")
    private String remark;

    @JsonView({Create.class})
    @Schema(description = "组织机构Id")
    private String orgId;

    private String dataTypeName;
    private String dataTypeSuffix;
    public List<DataEntryUploadFileDTO> uploadFileDTOList = new ArrayList<>();
    public List<DataEntryUploadDbDTO> uploadDbDTOList = new ArrayList<>();

}