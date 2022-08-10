package com.wkq.file.util

import java.io.File
import java.io.FileFilter


/**
 * @author wkq
 *
 * @date 2022年08月10日 13:38
 *
 *@des  自定义文件过滤器(类型过滤)
 *
 */

class CustomFileTypeFileter(type: String = "") : FileFilter {
    var type = type
    override fun accept(pathname: File?): Boolean {
        return pathname!!.length() > 0 && pathname.name.endsWith(type)
    }

}