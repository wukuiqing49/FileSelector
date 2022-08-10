package com.wkq.file.util


/**
 * @author wkq
 *
 * @date 2022年08月10日 12:55
 *
 *@des
 *
 */

object Constant {
    //最大个数
    const val SELECT_MAX_FILE_COUNT = "MAX_FILE_COUNT"

    const val EXTRA_START_DIRECTORY = "START_DIRECTORY"
    //文件大小限制
    const val EXTRA_MAX_FILE_SIZE = "MAX_FILE_SIZE"
    const val EXTRA_MAX_VIDEO_SIZE = "MAX_VIDEO_SIZE"
    const val EXTRA_MAX_IMAGE_SIZE = "MAX_IMAGE_SIZE"
    //选怎图片的类型
    const val EXTRA_SELECT_TYPE = "TYPE"
    const val EXTRA_RESULT_CODE = "RESULT_CODE"


    const val TYPE :String= "ALL"


    //聊天页面图片文件最大100M
    val IMAGE_MAX_SIZE: Long = 25 * 1024 * 1024

    //聊天页面文件最大100M
    val FILE_MAX_SIZE: Long = 100 * 1024 * 1024

    //聊天页面视频文件最大100M
    const val VIDEO_MAX_SIZE: Long = 100 * 1024 * 1024
    //最大个数
    const val MAX_NUM: Int = 9
    const val RESULT_CODE: Int = 10011

}