package com.surge.map.dataentry.upload;

import com.alibaba.fastjson.JSON;
import com.surge.common.core.constant.DictType;
import com.surge.common.core.constant.GISFileType;
import com.surge.common.core.constant.enums.GisFileType;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.threadpool.GlobalThreadPool;
import com.surge.common.core.utils.*;
import com.surge.common.core.utils.file.FileUtils;
import com.surge.common.gis.enums.EpsgEnum;
import com.surge.common.gis.model.DataField;
import com.surge.common.gis.ogr.VectorInfo;
import com.surge.common.gis.ogr.VectorUtils;
import com.surge.common.core.utils.JsonUtils;
import com.surge.map.service.IDataEntryService;
import com.surge.map.domain.entity.DataEntry;
import com.surge.map.domain.entity.DataEntrySet;
import com.surge.map.domain.enums.DataEntryError;
import com.surge.map.domain.enums.DataEntryStatus;
import com.surge.map.domain.dto.DataEntryUploadDTO;
import com.surge.map.domain.dto.DataEntryUploadFileDTO;
import com.surge.common.gis.vector.shp.ShapeDataStore;
import com.surge.map.service.IDataEntrySetService;
import com.surge.system.api.RemoteDictService;
import com.surge.system.domain.entity.SysDict;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShpFileRelease {

    @DubboReference
    private RemoteDictService remoteDictService;
    @Autowired
    private IDataEntryService dataEntryService;
    @Autowired
    private IDataEntrySetService dataEntrySetService;

    @Transactional(rollbackFor = Exception.class)
    public Boolean uploadData(DataEntryUploadDTO dataEntryUploadDTO) {
        if(dataEntryUploadDTO.getUploadFileDTOList().size() == 0 || Objects.isNull(dataEntryUploadDTO.getFiles())) {
            throw new ServiceException("上传文件列表为空，请检查修正后重新上传");
        }
        boolean anyMatch = dataEntryUploadDTO.getUploadFileDTOList().stream()
                .anyMatch(it -> StringUtils.isEmpty(it.getName()) || StringUtils.isEmpty(it.getFileName()));
        if (anyMatch) {
            throw new ServiceException("数据名称或文件名称参数存在为空，请检查修正后重新上传");
        }
        Map<String, Long> nameCollect = dataEntryUploadDTO.getUploadFileDTOList().stream()
                .map(DataEntryUploadFileDTO::getName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        nameCollect.forEach((k, v) ->{
            if (v > 1) {
                throw new ServiceException(String.format("数据名称【%s】重复，请检查修正后重新上传", k));
            }
        });
        Map<String, Long> fileNameCollect = dataEntryUploadDTO.getUploadFileDTOList().stream()
                .map(DataEntryUploadFileDTO::getFileName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        fileNameCollect.forEach((k, v) ->{
            if (v > 1) {
                throw new ServiceException(String.format("文件名称【%s】重复，请检查修正后重新上传", k));
            }
        });

        if (dataEntryUploadDTO.getFileType() == null) {
            throw new ServiceException("文件类型为空，请检查修正后重新上传");
        }
        for (MultipartFile file : dataEntryUploadDTO.getFiles()) {
            if (StringUtils.isBlank(file.getOriginalFilename()) || file.getSize() <= 0) {
                throw new ServiceException("上传文件名称或内容为空，请检查后重新上传");
            }
            String extension = FileUtils.getExtension(file.getOriginalFilename()); // 获取文件后缀名
            if (GISFileType.ZIP.equals(extension)) {   // 校验是否为压缩包
                try {   // 校验是否为多层压缩
                    if (ZipUtils.checkZip(file.getInputStream()).contains(GISFileType.ZIP)) {
                        throw new ServiceException("压缩包存在多次压缩情况，请检查修正后重新上传");
                    }
                } catch (IOException e) {
                    throw new ServiceException("压缩包解析失败！");
                }
            }
        }
        // 校验文件类型是否支持入库(字典表设置)
        SysDict sysDict = remoteDictService.selectByTypeAndValue(DictType.GIS_FILE_TYPE, String.valueOf(dataEntryUploadDTO.getFileType()));
        GisFileType fileTypeEnum = GisFileType.getFileSuffixEnum(sysDict.getLabel().toLowerCase());
        if (Objects.isNull(fileTypeEnum)) {
            throw new ServiceException("文件类型不支持入库，请检查修正后重新上传");
        }
        dataEntryUploadDTO.setDataTypeName(sysDict.getLabel());
        dataEntryUploadDTO.setDataTypeSuffix(sysDict.getAppend());

        // multipart转为InputStream，避免异步操作时tomcat自动删除
        for(MultipartFile multipartFile : dataEntryUploadDTO.getFiles()) {
            String fileName = multipartFile.getOriginalFilename();
            String extension = FilenameUtils.getExtension(fileName);
            Optional<DataEntryUploadFileDTO> dataEntryUploadFileDTO = dataEntryUploadDTO.uploadFileDTOList.stream()
                    .filter(it -> it.getFileName().equals(fileName))
                    .findFirst();
            try {
                if (!dataEntryUploadFileDTO.isPresent()) {
                    throw new NoSuchElementException("读取文件" + fileName + "错误，请检查后重新上传");
                }
                dataEntryUploadFileDTO.get().setInputStream(multipartFile.getInputStream()); // 获取文件输入流, 防止tomcat删除
                dataEntryUploadFileDTO.get().setFileSuffix(extension);
            } catch (IOException e) {
                throw new ServiceException("读取上传文件错误，请检查后重新上传");
            }
        }
        // 批量入库，状态设置为上传中
        for(DataEntryUploadFileDTO vo : dataEntryUploadDTO.getUploadFileDTOList()) {
            DataEntry dataEntry = new DataEntry();
            BeanUtils.copyPropertiesIgnoreNull(dataEntryUploadDTO, dataEntry);
            dataEntry.setName(vo.getName());
            dataEntry.setStatus(DataEntryStatus.PROCESSING.ordinal());
            dataEntryService.create(dataEntry);
            vo.setDataEntry(dataEntry);
        }
        // 异步处理
        this.asyncUpload(dataEntryUploadDTO);
        return true;
    }

    private void asyncUpload(DataEntryUploadDTO dataEntryUploadDTO) {
        GlobalThreadPool.EXECUTOR.execute(() -> {
            Thread.currentThread().setName("data-file-upload-" + System.currentTimeMillis());
            String localUploadDir = null; //本地上传文件目录

            try {
                localUploadDir = GISFileType.LOCAL + IdUtils.generateLongId(); // 本地临时路径
                FileUtils.mkdir(localUploadDir); // 创建当前批量提交目录
            } catch (Exception e) {
                for(DataEntryUploadFileDTO vo : dataEntryUploadDTO.getUploadFileDTOList()) {
                    DataEntry dataEntry = vo.getDataEntry();
                    dataEntry.setErrorLog("文件上传失败，请检查后重新上传");
                    dataEntry.setStatus(DataEntryStatus.FAILD.ordinal());
                    dataEntryService.update(dataEntry);
                    vo.setDataEntry(dataEntry);
                }
                return;
            }

            // 循环批量写入本地磁盘
            Iterator<DataEntryUploadFileDTO> iterator = dataEntryUploadDTO.uploadFileDTOList.iterator();
            while (iterator.hasNext()) {
                DataEntryUploadFileDTO dataEntryUploadFileDTO = iterator.next();
                DataEntry dataEntry = dataEntryUploadFileDTO.getDataEntry();
                String localFilePath = localUploadDir + File.separator + dataEntryUploadFileDTO.getFileName();
                dataEntryUploadFileDTO.setLocalFilePath(localFilePath);
                try {
                    File file = new File(localFilePath);
                    FileUtils.copy(dataEntryUploadFileDTO.getInputStream(), file);
                    if (GISFileType.ZIP.equals(dataEntryUploadFileDTO.getFileSuffix())) { // 校验是否为压缩包
                        String localUnzipFileDir = localUploadDir + File.separator + FilenameUtils.getBaseName(file.getName()); // 按照压缩包名称创建文件夹
                        FileUtils.mkdir(localUploadDir + File.separator + FilenameUtils.getBaseName(file.getName()));
                        ZipUtils.unzip(file, localUnzipFileDir); // 写入指定目录中
                        dataEntryUploadFileDTO.setLocalUnzipFileDir(localUnzipFileDir);
                    }
                } catch (Exception e) {
                    log.error("上传文件失败，请检查后重新上传", e);
                    dataEntry.setErrorLog("上传文件失败，无法正常解析，请检查文件！");
                    dataEntryService.update(dataEntry);
                    continue;
                } finally {
                    try {
                        if(dataEntryUploadFileDTO.getInputStream() != null) {
                            dataEntryUploadFileDTO.getInputStream().close(); // 关闭输入流
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    // 获取当前目录下文件
                    List<String> localUnzipFilePathList =  Arrays.stream(new File(dataEntryUploadFileDTO.getLocalUnzipFileDir()).listFiles())
//                            .filter(it -> fileName.equals(it.getName()))
                            .map(File::getPath)
                            .collect(Collectors.toList());
                    // 校验是否满足必传文件
                    Set<String> suffix;
                    GisFileType gisFileType = GisFileType.getFileSuffixEnum(dataEntryUploadDTO.getDataTypeName());
                    if (gisFileType.IS_3D_TILES.getSuffix().equals(dataEntryUploadDTO.getDataTypeName()) ||
                            gisFileType.IS_GLTF.getSuffix().equals(dataEntryUploadDTO.getDataTypeName())) {
                        String fullPathNoEndSeparator = FilenameUtils.getFullPathNoEndSeparator(localUnzipFilePathList.get(0));
                        suffix = FileUtils.getAllFileSuffix(new HashSet<>(), new File(fullPathNoEndSeparator));
                    } else {
                        suffix = localUnzipFilePathList.stream().map(FilenameUtils::getExtension).collect(Collectors.toSet());
                    }
                    List<String> filterList = JSON.parseObject(dataEntryUploadDTO.getDataTypeSuffix())
                            .getJSONArray("suffix").toJavaList(String.class)
                            .stream()
                            .filter(it ->
                                    !Arrays.stream(it.replaceAll("\\.", "").split("\\|"))
                                            .anyMatch(suffix::contains) // 只要有一个满足
                            ) // 如果存在，则不过滤
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(filterList)) { // 如果不为空
                        // 如果存在修改为失败状态
                        String errorLog = String.format(DataEntryError.CHECK_SUFFIX, gisFileType.getSuffix(), String.join(",", filterList));
                        dataEntry.setErrorLog(errorLog);
                        dataEntryService.update(dataEntry);
                        continue;
                    }
                    if(StringUtils.isNotEmpty(dataEntryUploadFileDTO.getCoordinateSystem())){
                        EpsgEnum epsg = EpsgEnum.getByLabel(dataEntryUploadFileDTO.getCoordinateSystem());
                        if(epsg == null) {
                        } else {
                            dataEntry.setEpsg(epsg.getCode());
                            dataEntry.setCoordinateSystem(dataEntryUploadFileDTO.getCoordinateSystem());
                            dataEntryService.update(dataEntry);
                        }
                    }
                    if(gisFileType.getName().toLowerCase(Locale.ROOT).equals("shp")) {
                        String fileUrl = localUnzipFilePathList.stream().findFirst()
                                .orElseThrow(() -> new ServiceException("压缩包存在文件夹，请检查修正后重新上传"));
                        String localShpFilePath = FilenameUtils.removeExtension(fileUrl) + GISFileType.SHP_SUFFIX;
                        if (!new File(localShpFilePath).isFile()) { // 获取文件中 .shp 文件路径
                            dataEntry.setErrorLog("压缩包存在文件夹，请检查修正后重新上传");
                            dataEntryService.update(dataEntry);
                            continue;
                        }
                        try(ShapeDataStore shapeDataStore = new ShapeDataStore(localShpFilePath)) {
                            VectorInfo vectorInfo = VectorUtils.getCentral(localShpFilePath); // 读取空间坐标系
                            dataEntry.setEpsg(vectorInfo.getEpsg());
                            List<DataField> fieldList = shapeDataStore.getSchema(); //shp数据解析保存到pg数据库
                            List<Map<String,Object>> data = shapeDataStore.getData(fieldList);
                            List<DataEntrySet> dataEntrySetList = new ArrayList<>(data.size());
                            for(Map map : data) {
                                // 数据库插入数据
                                DataEntrySet dataEntrySet = new DataEntrySet();
                                dataEntrySet.setId(IdUtils.generateLongId());
                                dataEntrySet.setDataEntryId(dataEntry.getId());
                                dataEntrySet.setGeom(map.get("geometry").toString());
                                map.remove("geometry");
                                dataEntrySet.setFieldInfo(JsonUtils.getObjectMapper().valueToTree(map));
                                dataEntrySetList.add(dataEntrySet);
                            }
                            dataEntrySetService.batchSave(dataEntrySetList);
                            dataEntry.setDataSchema(JsonUtils.getObjectMapper().valueToTree(fieldList)); // 设置上传文件模型
                            dataEntry.setDataAccess(null); // 需要将文件上传到seaweedFS
                            dataEntry.setDataCentral(null); // 中心点
                            dataEntry.setDataExtent(null);  // 边界
                            dataEntry.setErrorLog("0");
                            dataEntry.setStatus(DataEntryStatus.COMPLETE.ordinal());
                        } catch (Exception e) {
                            log.error("数据上传失败", e);
                            dataEntry.setErrorLog("shp文件解析失败");
                            dataEntry.setStatus(DataEntryStatus.FAILD.ordinal());
                        } finally {
                            dataEntryService.update(dataEntry);
                        }
                    }
                } catch (Exception e) {
                    log.error("数据上传失败", e);
                    dataEntry.setStatus(DataEntryStatus.FAILD.ordinal());
                    dataEntryService.update(dataEntry);
                }
            }

        // 删除临时文件
        FileUtils.delete(localUploadDir);
    });
    }
}
