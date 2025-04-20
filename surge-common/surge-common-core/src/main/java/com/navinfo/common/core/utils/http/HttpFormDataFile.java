package com.surge.common.core.utils.http;

import lombok.Data;

import java.io.File;

@Data
public class HttpFormDataFile {

    public String fieldName; // 字段名称

    public String contentType;

    public String fileName; // 原文件名
    public byte[] fileData;
    public File file;

    private HttpFormDataFile() {}

    public HttpFormDataFile(String fieldName, String fileName, byte[] fileData) {
        if(isBlank(fieldName)){
            this.fieldName = "file";
        } else {
            this.fieldName = fieldName;
        }

        if(isBlank(fileName)){
            this.fileName = "file";
        } else {
            this.fileName = fileName;
        }

        if(isBlank(contentType)) {
            this.contentType = "application/octet-stream";
        } else {
            this.contentType = contentType;
        }

        this.fileData = fileData;
    }

    public HttpFormDataFile(String fieldName, String fileName, File file) {
        this.fieldName = fieldName;
        if(isBlank(this.fieldName)){
            this.fieldName = "file";
        }

        this.fileName = "file";
        if(file != null) {
            this.fileName = file.getName();
        }
        if(!isBlank(this.fileName)){
            this.fileName = fileName;
        }

        if(isBlank(contentType)) {
            this.contentType = "application/octet-stream";
        }

        this.file = file;
    }

    private static boolean isBlank(String str) {
        if (str == null || str.trim().isEmpty()) {
            return true;
        }
        return false;
    }
}
