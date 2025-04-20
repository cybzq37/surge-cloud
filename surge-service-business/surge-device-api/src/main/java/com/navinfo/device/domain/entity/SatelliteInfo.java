package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Tag(name = "卫星信息")
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("nc_device.satellite_info")
public class SatelliteInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "组织机构Id", type = "String")
    private String orgId;

    @Schema(description = "基站Epid", type = "String")
    private String stationEpid;

    @Schema(description = "卫星类型", type = "String")
    private String type;

    @Schema(description = "卫星编号", type = "String")
    private String number;

    @Schema(description = "仰角", type = "String")
    private String elevation;

    @Schema(description = "方位角", type = "String")
    private String azimuth;

    @Schema(description = "信噪比", type = "String")
    private String ratio;

    @Schema(description = "更新时间", type = "Date")
    private Date updateTime;

}
