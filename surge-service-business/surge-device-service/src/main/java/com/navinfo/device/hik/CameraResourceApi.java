package com.surge.device.hik;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.surge.device.domain.bean.CameraIndexBean;
import com.surge.device.domain.bean.JsonRootBean;
import com.surge.device.domain.bean.RegionIndexBean;
import com.surge.device.domain.bean.RootRegionData;
import com.surge.device.domain.entity.HikCamera;
import com.surge.device.domain.entity.HikRegion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class CameraResourceApi {

    /**
     * 固定路径
     */
    private static final String ARTEMIS_PATH = "/artemis";

    /**
     * 设置参数提交方式
     */
    private static final String contentType = "application/json";

    public static final ArtemisConfig config = new ArtemisConfig();
    static {
//        config.setHost("localhost:55443");
        config.setHost("10.213.55.55:443");             // 代理API网关nginx服务器ip端口(南京港)
        config.setAppKey("23740076");                   // 秘钥appkey
        config.setAppSecret("bARrjeiDILfhr5i44Psl");    // 秘钥appSecret
    }

    /**
     * {"code":"0","msg":"success","data":{"indexCode":"root000000","name":"南京港视频监控平台","parentIndexCode":"-1","treeCode":"0"}}
     * 获取根区域信息
     * @return
     */
    public String getRegionsRoot() throws Exception {
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", ARTEMIS_PATH + "/api/resource/v1/regions/root");
            }
        };
        Map<String, String> paramMap = new HashMap<>();
        String body = JSON.toJSONString(paramMap);
        return ArtemisHttpUtil.doPostStringArtemis(config, path, body, null, null, "application/json");
    }

    /**
     * 获取根区域信息
     * @return
     */
    public String getRegionsSub(int pageNo, int pageSize, String parentIndexCode) throws Exception {
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", ARTEMIS_PATH + "/api/resource/v2/regions/subRegions");
            }
        };
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("parentIndexCode", parentIndexCode); //root000000
        paramMap.put("resourceType", "camera");
        paramMap.put("pageNo", pageNo);
        paramMap.put("pageSize", pageSize);
        String body = JSON.toJSONString(paramMap);
        return ArtemisHttpUtil.doPostStringArtemis(config, path, body, null, null, "application/json");
    }


    /**
     * 根据区域编号获取下级监控点列表
     *
     * @return
     */
    private String getCamereIndexCode(int pageNo, int pageSize, String regionIndexCode) throws Exception {
        final String previewURLsApi = ARTEMIS_PATH + "/api/irds/v2/resource/subResources";
        Map<String, String> path = new HashMap<String, String>(10) {
            {
                put("https://", previewURLsApi);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("regionIndexCode", regionIndexCode);
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        jsonBody.put("authCodes", Arrays.asList("view"));
        jsonBody.put("resourceType", "camera");
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(config, path, body, null, null, contentType);// post请求application/json类型参数
        return result;
    }

    /**
     * 递归遍历摄像头
     *
     * @param indexCode
     * @param exportList
     */
    public void showAllCameras(String indexCode, List<HikCamera> exportList) throws Exception {
        String result = getCamereIndexCode(1, 100, indexCode);

        JSONObject root = JSONObject.parseObject(result);
        // 成功返回
        if (null != root && "0".equals(root.getString("code"))) {
            JSONObject data = root.getJSONObject("data");
            if (data != null && data.getIntValue("total") > 0) {
                for(int i=0; i<data.getJSONArray("list").size(); i++) {
                    HikCamera cameraIndexBean = data.getJSONArray("list").getObject(i, HikCamera.class);
                    exportList.add(cameraIndexBean);
                }
            }
        }
    }

    /**
     * 遍历组织树，根据根目录获取所有的叶子节点
     *
     * @return
     */
    public void showNoAllRegions(String rootIndex, List<RegionIndexBean> regionIndexBeans) throws Exception {
        String regions = getRegionsSub(1, 100, rootIndex);
        JSONObject root = JSON.parseObject(regions);
        JSONObject data = root.getJSONObject("data");
        // 返回区域信息
        if (data != null && data.getIntValue("total") > 0) {
            for(int i=0; i<data.getJSONArray("list").size(); i++) {
                RegionIndexBean bean = data.getJSONArray("list").getObject(i, RegionIndexBean.class);
                if (!bean.isLeaf()) {
                    regionIndexBeans.add(bean);
                } else {
                    showNoAllRegions(bean.getIndexCode(), regionIndexBeans);
                }
            }
        }
    }

    /**
     * 遍历组织树，根据根目录获取所有的叶子节点
     *
     * @return
     */
    public void showAllRegions(String rootIndex, List<HikRegion> regionIndexBeans) throws Exception {
        String regions = getRegionsSub(1, 100, rootIndex);
        JSONObject root = JSON.parseObject(regions);
        JSONObject data = root.getJSONObject("data");
        // 返回区域信息
        if (data != null && data.getIntValue("total") > 0) {
            for(int i=0; i<data.getJSONArray("list").size(); i++) {
                HikRegion bean = data.getJSONArray("list").getObject(i, HikRegion.class);
                if (bean.getLeaf().equals(1)) {
                    regionIndexBeans.add(bean);
                } else {
                    regionIndexBeans.add(bean);
                    showAllRegions(bean.getIndexCode(), regionIndexBeans);
                }
            }
        }
    }

    public void showAllRegions(String rootIndex,String indexCode, List<RegionIndexBean> regionIndexBeans) throws Exception {
        String regions = getRegionsSub(1, 100, rootIndex);
        JSONObject root = JSON.parseObject(regions);
        JSONObject data = root.getJSONObject("data");
        // 返回区域信息
        if (data != null && data.getIntValue("total") > 0) {
            for(int i=0; i<data.getJSONArray("list").size(); i++) {
                RegionIndexBean bean = data.getJSONArray("list").getObject(i, RegionIndexBean.class);
                if (bean.isLeaf()) {
                    if(bean.getParentIndexCode().equals(indexCode)){
                        regionIndexBeans.add(bean);
                    }
                } else {
                    showAllRegions(bean.getIndexCode(), indexCode, regionIndexBeans);
                }
            }
        }
    }

    /**
     * STEP2：组装请求参数
     * 取流协议（应用层协议），
     * "hik":HIK私有协议，使用视频SDK进行播放时，传入此类型；
     * "rtsp":RTSP协议；
     * "rtmp":RTMP协议；
     * "hls":HLS协议（HLS协议只支持海康SDK协议、EHOME协议、ONVIF协议接入的设备；只支持H264视频编码和AAC音频编码）。
     * 参数不填，默认为HIK协议
     * <p>
     * 参考：https://open.hikvision.com/docs/69d3d37a56ec4d24a6596c3e6ed436af#b5bd6fd9
     */
    public String GetCameraPreviewURL(String cameraIndexCode, String protocol) throws Exception {
        Map<String, String> path = new HashMap<String, String>(10) {
            {
                put("https://", ARTEMIS_PATH + "/api/video/v1/cameras/previewURLs");
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        // 码流类型，0:主码流 1:子码流 2:第三码流
        jsonBody.put("streamType", 1);
        // 取流协议（应用层协议）
        jsonBody.put("protocol", protocol);
        // 传输协议（传输层协议），0:UDP 1:TCP 默认是TCP
        jsonBody.put("transmode", 1);
//        jsonBody.put("expand", "streamform=ps");
        jsonBody.put("streamform", "rtp");
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(config, path, body, null, null, contentType);// post请求application/json类型参数
        return result;
    }

}
