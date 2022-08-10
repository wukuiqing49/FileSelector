package com.wkq.file.util;

import android.content.Intent;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 选择结果结果
 */
public class FilePickerResult implements Serializable {
    public static final String RESULT = "result";

    public FilePickerResult(List<File> fileList) {
        this.fileList = fileList;
    }

    private List<File> fileList;

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public static FilePickerResult getPickerResult(Intent intent) {
        return (FilePickerResult) intent.getSerializableExtra(RESULT);
    }
}
