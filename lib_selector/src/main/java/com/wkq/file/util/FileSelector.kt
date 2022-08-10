package com.wkq.file.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.wkq.file.FileSelectorActivity


/**
 * @author wkq
 *
 * @date 2022年08月10日 14:04
 *
 *@des  文件选择构建者
 *
 */

class FileSelector() {
    private var maxImagSize = Constant.IMAGE_MAX_SIZE
    private var maxFileSize = Constant.FILE_MAX_SIZE
    private var maxVideoSize = Constant.VIDEO_MAX_SIZE
    private var maxNum = Constant.MAX_NUM
    private var typeList = ArrayList<String>()

    companion object {
        fun builder(): FileSelectorBulider {
            return FileSelectorBulider()
        }
    }

    class FileSelectorBulider() {

        private val fileSelector = FileSelector()

        fun setMaxImagSize(maxImagSize: Long): FileSelectorBulider {
            fileSelector.maxImagSize = maxImagSize
            return this
        }

        fun setMaxFileSizee(maxFileSize: Long): FileSelectorBulider {
            fileSelector.maxFileSize = maxFileSize
            return this
        }

        fun setMaxVideoSize(maxVideoSize: Long): FileSelectorBulider {
            fileSelector.maxVideoSize = maxVideoSize
            return this
        }

        /**
         *  最大个数
         */
        fun setMaxNum(maxNum: Int): FileSelectorBulider {
            fileSelector.maxNum = maxNum
            return this
        }

        /**
         * type 格式 .apk .jpeg
         */
        fun setFileType(type: ArrayList<String>): FileSelectorBulider {
            if (type==null)return this
            fileSelector.typeList = type
            return this
        }

        fun bulid(): FileSelector {
            return fileSelector

        }

    }

    /**
     * type 格式 .apk .jpeg
     */
    fun startActivity(mContext: Activity, requestCode: Int,resultCode: Int=10011) {
        var intent = Intent(mContext, FileSelectorActivity::class.java)
        intent.putExtra(Constant.EXTRA_MAX_FILE_SIZE, maxFileSize)
        intent.putExtra(Constant.EXTRA_MAX_VIDEO_SIZE, maxVideoSize)
        intent.putExtra(Constant.EXTRA_MAX_IMAGE_SIZE, maxImagSize)
        intent.putExtra(Constant.SELECT_MAX_FILE_COUNT, maxNum)
        if (typeList.size==0){
            typeList.add(Constant.TYPE)
        }
        intent.putExtra(Constant.EXTRA_SELECT_TYPE, typeList)
        intent.putExtra(Constant.EXTRA_RESULT_CODE, resultCode)
        mContext.startActivityForResult(intent, requestCode)
    }

}