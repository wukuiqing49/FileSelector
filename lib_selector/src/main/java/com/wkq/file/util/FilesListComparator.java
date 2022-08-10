package com.wkq.file.util;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.io.File;
import java.util.Comparator;
import java.util.Locale;

/**
 * 文件 排序 文字 文件夹比较
 */
public class FilesListComparator implements Comparator<File> {
    @Override
    @IntRange(from = -1, to = 1)
    public int compare(@NonNull File file1, @NonNull File file2) {
        try {
            if (file1 == null || file1.length() == 0 || !file1.exists()) return -1;
            if (file2 == null || file2.length() == 0 || !file2.exists()) return 1;
            if (file1.isDirectory() && !file2.isDirectory()) {
                return -1;
            } else if (!file1.isDirectory() && file2.isDirectory()) {
                return 1;
            } else if (!file1.isDirectory() && !file2.isDirectory()) {
                if (file1.lastModified() == file2.lastModified()) {
                    return 0;
                } else if (file1.lastModified() > file2.lastModified()) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return file1.getName().toLowerCase(Locale.getDefault()).compareTo(file2.getName().toLowerCase(Locale.getDefault()));
            }
        } catch (Exception e) {
            return 0;
        }
    }
}