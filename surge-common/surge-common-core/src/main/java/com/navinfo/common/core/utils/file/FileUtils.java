package com.surge.common.core.utils.file;

import cn.hutool.core.io.FileUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 文件处理工具类
 *
 * @author lichunqing
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static void copy(InputStream inputStream, File file) throws IOException {
        org.apache.commons.io.FileUtils.copyToFile(inputStream, file);
    }

    public static void copy(InputStream inputStream, String filePath) throws IOException {
        org.apache.commons.io.FileUtils.copyToFile(inputStream, new File(filePath));
    }

    public static File mkdir(String dirPath) {
        return FileUtil.mkdir(dirPath);
    }

    public static void delete(String dirPath) {
        FileUtil.del(dirPath);
    }

    public static File file(String filepath) {
        return FileUtil.file(filepath);
    }

    public static String getExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    public static String getFileName(String fileName) {
        return FileUtils.getFileName(fileName);
    }

    public static boolean exist(File file) {
        return FileUtil.exist(file);
    }

    public static boolean exist(String filename) {
        return FileUtil.exist(filename);
    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     * @return
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
            .append(percentEncodedFileName)
            .append(";")
            .append("filename*=")
            .append("utf-8''")
            .append(percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    public static void searchFiles(ArrayList<File> fileList, File root) {
        File[] files = root.listFiles();
        if (files == null) {
            return;
        }
        int length = files.length;
        for (int i = 0; i < length; i++) {
            if (files[i].isDirectory()) {
                searchFiles(fileList, files[i]);
            } else {
                fileList.add(files[i]);
            }
        }
    }

    public static CountFile countFiles(File pathName) {
        ArrayList<File> fileList = new ArrayList<>();
        if (pathName.isFile()) {
            fileList.add(pathName);
        }
        searchFiles(fileList, pathName);
        long totalSize = 0;
        for (int i = 0; i < fileList.size(); i++) {
            totalSize += fileList.get(i).length();
        }
        return new CountFile(fileList.size(), totalSize);
    }

    public static CountFile countFiles(File pathName, String fileName) {
        ArrayList<File> fileList = new ArrayList<>();
        File[] files = pathName.listFiles();
        if (files == null) {
            return new CountFile();
        }
        for (File file : files) {
            if (FilenameUtils.getBaseName(file.getName()).equals(fileName)) {
                fileList.add(file);
            }
        }
        long totalSize = 0;
        for (File file : fileList) {
            totalSize += file.length();
        }
        return new CountFile(fileList.size(), totalSize);
    }


    public static CountFile countFiles(List<String> listPath) {
        long totalSize = 0;
        for (int i = 0; i < listPath.size(); i++) {
            totalSize += new File(listPath.get(i)).length();
        }
        return new CountFile(listPath.size(), totalSize);
    }

    public static Set<String> getAllFileSuffix(Set<String> listPath, File file) {
        File[] files = file.listFiles();
        if (files == null) {
            return listPath;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                getAllFileSuffix(listPath, files[i]);
            } else {
                listPath.add(FilenameUtils.getExtension(files[i].getName()));
            }
        }
        return listPath;
    }
}
