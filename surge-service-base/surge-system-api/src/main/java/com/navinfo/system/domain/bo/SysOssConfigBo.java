package com.surge.system.domain.bo;

import com.surge.common.core.domain.BaseEntity;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Update;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 对象存储配置业务对象 sys_oss_config
 *
 * @author lichunqing
 * @author 孤舟烟雨
 * @date 2021-08-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysOssConfigBo extends BaseEntity {

    /**
     * 主建
     */
    @NotNull(message = "主建不能为空", groups = {Update.class})
    private Long ossConfigId;

    /**
     * 配置key
     */
    @NotBlank(message = "配置key不能为空", groups = {Create.class, Update.class})
    @Size(min = 2, max = 100, message = "configKey长度必须介于2和20 之间")
    private String configKey;

    /**
     * accessKey
     */
    @NotBlank(message = "accessKey不能为空", groups = {Create.class, Update.class})
    @Size(min = 2, max = 100, message = "accessKey长度必须介于2和100 之间")
    private String accessKey;

    /**
     * 秘钥
     */
    @NotBlank(message = "secretKey不能为空", groups = {Create.class, Update.class})
    @Size(min = 2, max = 100, message = "secretKey长度必须介于2和100 之间")
    private String secretKey;

    /**
     * 桶名称
     */
    @NotBlank(message = "桶名称不能为空", groups = {Create.class, Update.class})
    @Size(min = 2, max = 100, message = "bucketName长度必须介于2和100之间")
    private String bucketName;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 访问站点
     */
    @NotBlank(message = "访问站点不能为空", groups = {Create.class, Update.class})
    @Size(min = 2, max = 100, message = "endpoint长度必须介于2和100之间")
    private String endpoint;

    /**
     * 自定义域名
     */
    private String domain;

    /**
     * 是否https（Y=是,N=否）
     */
    private String isHttps;

    /**
     * 是否默认（0=是,1=否）
     */
    private String status;

    /**
     * 域
     */
    private String region;

    /**
     * 扩展字段
     */
    private String ext1;

    /**
     * 备注
     */
    private String remark;

    /**
     * 桶权限类型(0private 1public 2custom)
     */
    @NotBlank(message = "桶权限类型不能为空", groups = {Create.class, Update.class})
    private String accessPolicy;


}
