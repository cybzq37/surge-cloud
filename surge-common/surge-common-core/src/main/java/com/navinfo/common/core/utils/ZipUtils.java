package com.surge.common.core.utils;

import com.surge.common.core.constant.GISFileType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class ZipUtils {


    public static void unzip(File zipFile, String descDir) throws Exception {
        ZipArchiveInputStream inputStream = getZipFile(zipFile);
        unzip(descDir, inputStream);
        inputStream.close();
    }


    public static void unzip(InputStream zipFile, String descDir) {
        try (ZipArchiveInputStream inputStream = getZipFile(zipFile)) {
            unzip(descDir, inputStream);
            log.info("******************解压完毕********************");
        } catch (Exception e) {
            log.error("[unzip] 解压zip文件出错", e);
            throw new RuntimeException(e);
        }
    }

    private static void unzip(String descDir, ZipArchiveInputStream inputStream) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipArchiveEntry entry = null;
        while ((entry = inputStream.getNextZipEntry()) != null) {
            if (!entry.getName().toLowerCase().startsWith(GISFileType.MAC_OSX)) {
                String filePath = descDir + File.separator + entry.getName();
                if (entry.isDirectory()) {
                    File directory = new File(filePath);
                    directory.mkdirs();
                } else {
                    if (!new File(FilenameUtils.getFullPathNoEndSeparator(filePath)).exists()) {
                        File directory = new File(FilenameUtils.getFullPathNoEndSeparator(filePath));
                        directory.mkdirs();
                    }
                    OutputStream os = null;
                    try {
                        os = new BufferedOutputStream(new FileOutputStream(filePath));
                        //输出文件路径信息
//                            log.info("解压文件的当前路径为:{}", filePath);
                        IOUtils.copy(inputStream, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        }
    }

    public static String getFileList(String path, String fileLastFix, String name) {
        File file = new File(path);

        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        String[] fixList = fileLastFix.split(";");
        String[] files = file.list();
        for (String fileName : files) {
            File tfile = new File(path + fileName);
            if (tfile.isDirectory()) {
                getFileList(tfile.getAbsolutePath(), fileLastFix, fileName);
            } else if (tfile.isFile()) {
                String strFix = fileName.substring(fileName.lastIndexOf("."));
                if (GISFileType.FILE.equals(name)) {
                    if (exists(fileName, fixList)) {
                        return tfile.getAbsolutePath();
                    }
                } else {
                    if (exists(strFix, fixList)) {
                        return tfile.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }

    public static boolean exists(String sourcee, String[] strList) {
        if (strList == null || strList.length == 0) {
            return false;
        }
        for (String str : strList) {
            if (str.equalsIgnoreCase(sourcee)) {
                return true;
            }
        }
        return false;
    }

    private static ZipArchiveInputStream getZipFile(File zipFile) throws Exception {
        return new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)), Charset.forName("GBK").name());
    }

    private static ZipArchiveInputStream getZipFile(InputStream zipFile) {
        return new ZipArchiveInputStream(new BufferedInputStream(zipFile), Charset.forName("GBK").name());
    }

    /**
     * 压缩文件
     * @param sourceFolderPath 压缩目录获文件
     * @param zipFilePath 压缩包路径
     */
    public static void ZipFolderExample (String sourceFolderPath, String zipFilePath) {
        try {
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipArchiveOutputStream zos = new ZipArchiveOutputStream(fos);
            File sourceFolder = new File(sourceFolderPath);
            addFolderToZip(sourceFolder, null, zos);
            zos.flush();
            zos.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addFolderToZip(File folder, String parentFolderName, ZipArchiveOutputStream zos) throws IOException {
        File[] files = new File[1];
        if (folder.isFile()) {
             files[0] = new File(folder.getPath());
        }else {
            files = folder.listFiles();
        }
        for (File file : files) {
            if (file.isDirectory()) {
                addFolderToZip(file, (StringUtils.isNotBlank(parentFolderName) ? parentFolderName + File.separator : "") + file.getName(), zos);
                continue;
            }
            FileInputStream fis = new FileInputStream(file);
            ZipArchiveEntry zipEntry = new ZipArchiveEntry((StringUtils.isNotBlank(parentFolderName) ? parentFolderName + File.separator : "") +  file.getName());
            zos.putArchiveEntry(zipEntry);
            IOUtils.copy(fis, zos);
            zos.closeArchiveEntry();
            fis.close();
        }
    }


    /**
     * 获取压缩包中的文件
     *
     * @param input
     * @return
     */
    public static List<String> checkZip(InputStream input) {
        List<String> fileList = new ArrayList<>();
        try {
            //获取ZIP输入流  指定字符集为 GBK
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), Charset.forName("GBK"));
            //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
            ZipEntry zipFile;
            while ((zipFile = zipInputStream.getNextEntry()) != null) {
                if (!zipFile.getName().toLowerCase().startsWith(GISFileType.MAC_OSX)) {
                    if (!zipFile.isDirectory()) {
                        // 文件名称
                        String fileName = zipFile.getName();
                        long fileSize = zipFile.getSize();
                        // 最后修改时间
                        FileTime time = zipFile.getLastModifiedTime();
                        log.info("文件名称" + fileName + " 文件大小" + fileSize + " 最后修改时间" + time);
                        fileList.add(FilenameUtils.getExtension(fileName));
                    }
                }
                zipInputStream.closeEntry();
            }
        } catch (Exception e) {
            try {
                input.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            log.error("异常");
        }
        return fileList;
    }
}
