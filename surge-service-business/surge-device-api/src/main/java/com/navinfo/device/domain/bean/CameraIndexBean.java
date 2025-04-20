package com.surge.device.domain.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class CameraIndexBean {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "所属区域唯一标识")
    private String regionIndexCode;

    @Schema(description = "区域名称")
    private String regionName;

    @Schema(description = "监控点唯一标识")
    private String indexCode;

    @Schema(description = "监控点名称")
    private String name;

    @Schema(description = "所属区域路径")
    private String regionPath;

    /**
     * 所属区域路径，由唯一标示组成，最大10级，格式： @根节点@子区域1@子区域2@
     */
    @Schema(description = "区域路径名称")
    private String regionPathName;

    /**
     * 监控点类型
     * 枪机0
     * 半球1
     * 快球2
     * 带云台枪机3
     */
    @Schema(description = "监控点类型")
    private int cameraType;

    @Schema(description = "监控点类型说明")
    private String cameraTypeName;

    @Schema(description = "设备能力集")
    private String capabilitySet;

    @Schema(description = "能力集说明")
    private String capabilitySetName;

    @Schema(description = "通道编号")
    private String channelNo;

    @Schema(description = "通道类型")
    private String channelType;

    @Schema(description = "通道类型说明")
    private String channelTypeName;

    @Schema(description = "所属设备标识")
    private String encodeDevIndexCode;

    @Schema(description = "监控点国标编号")
    private String gbIndexCode;

    @Schema(description = "安装位置")
    private String installLocation;

    @Schema(description = "键盘控制码")
    private String keyBoardCode;

    @Schema(description = "经度")
    private String longitude;

    @Schema(description = "纬度")
    private String latitude;

    @Schema(description = "海拔")
    private String altitude;

    @Schema(description = "摄像机像素")
    private String pixel;

    @Schema(description = "云镜类型")
    private String ptz;

    @Schema(description = "录像存储位置")
    private String recordLocation;

    @Schema(description = "录像存储位置说明")
    private String recordLocationName;

    @Schema(description = "在线状态")
    private String status;

    @Schema(description = "状态说明")
    private String statusName;

    @Schema(description = "传输协议类型")
    private int transType;

    @Schema(description = "传输协议类型说明")
    private String transTypeName;

    @Schema(description = "接入协议")
    private String treatyType;

    @Schema(description = "显示顺序")
    private int disOrder;

    @Schema(description = "所属平台ID")
    private String isc;

    private String parentIndexCode;
    private String externalIndexCode;
    private int chanNum;
    private String dacIndexCode;
    private String capability;
    private String decodeTag;
    private String comId;
    private String resourceType;
    private Date createTime;
    private Date updateTime;
    private String cameraRelateTalk;
    private int cascadeType;

    @Override
    public String toString() {


//        return
//                "所属区域唯一标识=" + regionIndexCode + '\t' +
//                        "区域名称=" + regionName + '\t' +
//                        "监控点唯一标识=" + indexCode + '\t' +
//                        "监控点名称=" + name + '\t' +
//                        "所属区域路径=" + regionPath + '\t' +
//                        "区域路径名称=" + regionPathName + '\t' +
//                        "监控点类型=" + cameraType + '\t' +
//                        "监控点类型说明=" + cameraTypeName + '\t' +
//                        "设备能力集=" + capabilitySet + '\t' +
//                        "能力集说明=" + capabilitySetName + '\t' +
//                        "通道编号=" + channelNo + '\t' +
//                        "通道类型=" + channelType + '\t' +
//                        "通道类型说明=" + channelTypeName + '\t' +
//                        "所属设备标识=" + encodeDevIndexCode + '\t' +
//                        "监控点国标编号=" + gbIndexCode + '\t' +
//                        "安装位置=" + installLocation + '\t' +
//                        "键盘控制码=" + keyBoardCode + '\t' +
//                        "纬度=" + latitude + '\t' +
//                        "经度=" + longitude + '\t' +
//                        "海拔=" + altitude + '\t' +
//                        "摄像机像素=" + pixel + '\t' +
//                        "云镜类型=" + ptz + '\t' +
//                        "录像存储位置=" + recordLocation + '\t' +
//                        "录像存储位置说明=" + recordLocationName + '\t' +
//                        "在线状态=" + status + '\t' +
//                        "状态说明=" + statusName + '\t' +
//                        "传输协议=" + transType + '\t' +
//                        "传输协议说明=" + transTypeName + '\t' +
//                        "接入协议=" + treatyType + '\t' +
//                        "显示顺序=" + disOrder + '\t'
//                ;


        return regionIndexCode + '\t' +
                regionName + '\t' +
                indexCode + '\t' +
                name + '\t' +
                regionPath + '\t' +
                regionPathName + '\t' +
                cameraType + '\t' +
                cameraTypeName + '\t' +
                capabilitySet + '\t' +
                capabilitySetName + '\t' +
                channelNo + '\t' +
                channelType + '\t' +
                channelTypeName + '\t' +
                encodeDevIndexCode + '\t' +
                gbIndexCode + '\t' +
                installLocation + '\t' +
                keyBoardCode + '\t' +
                latitude + '\t' +
                longitude + '\t' +
                altitude + '\t' +
                pixel + '\t' +
                ptz + '\t' +
                recordLocation + '\t' +
                recordLocationName + '\t' +
                status + '\t' +
                statusName + '\t' +
                transType + '\t' +
                transTypeName + '\t' +
                treatyType + '\t' +
                disOrder + '\t'
                ;
    }
}
