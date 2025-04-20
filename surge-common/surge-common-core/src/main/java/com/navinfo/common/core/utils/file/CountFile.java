package com.surge.common.core.utils.file;

public class CountFile {
    private long folderSize = 0;
    private long fileNum = 0;

    public CountFile(int fileNum, long totalSize) {
        this.fileNum = fileNum;
        this.folderSize = totalSize;
    }

    public CountFile() {
    }

    public long getFolderSize() {
        return folderSize;
    }

    public void setFolderSize(long folderSize) {
        this.folderSize = folderSize;
    }

    public long getFileNum() {
        return fileNum;
    }

    public void setFileNum(long fileNum) {
        this.fileNum = fileNum;
    }
}