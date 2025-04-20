package com.surge.common.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.json.view.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 *
 * @author lichunqing
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonView(Query.class)
    @Schema(description = "创建人", type = "String")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @JsonView(Query.class)
    @Schema(description = "创建时间", type = "Date")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonView(Query.class)
    @Schema(description = "更新人", type = "String")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @JsonView(Query.class)
    @Schema(description = "更新时间", type = "Date")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
