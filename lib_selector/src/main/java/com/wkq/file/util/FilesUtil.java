package com.wkq.file.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


import com.wkq.file.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * author : wkq
 * date   : 2019/11/13 15:58
 * desc   :
 */
public class FilesUtil {

    public static long maxImageSize;
    public static long maxVideoSize;
    public static long maxFileSize;


    public static File copyFile(Context context, Uri itemUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(itemUri);
            if (inputStream == null) return null;
            if (dstFile == null) return null;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
            return dstFile;
        } catch (Exception e) {
            return null;
        }
    }

    private static void copyStream(InputStream inputStream, OutputStream outputStream) throws Exception {
        byte[] data = new byte[1024 * 5];
        int temp = 0;
        while ((temp = inputStream.read(data)) != -1) {
            outputStream.write(data, 0, temp);
        }
    }

    public static boolean checkFileSize(Context mContext, long fileSize, String fileName) {
        if (fileSize <= 0) {
            Toast.makeText(mContext,mContext.getString(R.string.min_file_size),Toast.LENGTH_SHORT).show();

            return false;
        }
        boolean canSelect = canSelect(fileSize, fileName);
        if (canSelect) {
            return true;
        } else {
            Toast.makeText(mContext,mContext.getString(R.string.max_file_size),Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    //动态处理限制文件大小
    private static boolean canSelect(long fileSize, String fileName) {
        if (FilePickerUtils.isImage(fileName)) {
            if (fileSize <= maxImageSize)
                return true;
        } else if (FilePickerUtils.isVideo(fileName)) {
            if (fileSize <= maxVideoSize)
                return true;
        } else {
            if (fileSize <= maxFileSize)
                return true;
        }
        return false;
    }

}
