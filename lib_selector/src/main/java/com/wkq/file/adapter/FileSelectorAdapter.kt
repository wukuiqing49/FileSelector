package com.wkq.file.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wkq.file.R
import com.wkq.file.databinding.ItemFileBinding
import com.wkq.file.util.FilePickerUtils
import com.wkq.file.util.FilesUtil
import java.io.File


/**
 * @author wkq
 *
 * @date 2022年08月04日 13:05
 *
 * @des  拖拽的Adapter
 *
 * @param limitPosition 禁止操作的条目位置 默认第一个
 *
 */

class FileSelectorAdapter(mContext: Context, maxNum: Int = 9) : BaseRecyclerAdapter<File>(mContext) {

    //上下文
    var mContext = mContext
    var maxNum=maxNum

    // 固定条目的位置
    //内容数据
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileAdapterViewHolder {
        var binding = DataBindingUtil.inflate<ItemFileBinding>(
            LayoutInflater.from(mContext), R.layout.item_file, parent, false
        )
        var holder = FileAdapterViewHolder(binding.root)
        holder.setDataBinding(binding)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bindingHolder = holder as FileAdapterViewHolder
        var binding = bindingHolder.getBinding() as ItemFileBinding
        var file: File = getItem(position) ?: return
        val childFiles = file.listFiles()
        //简介  文件展示 文件更新时间以及大小  文件夹展示文件个数
        val desc = if (file.isFile) FilePickerUtils.getDataSize(file.length())
            .toString() + " " + FilePickerUtils.getModifiedTime(
            file
        ) else "文件：" + (childFiles?.size ?: 0)
        binding.desc.text = desc
        //设置图标
        binding.icon.setImageDrawable(
            FilePickerUtils.getDrawable(
                mContext, file.isDirectory, file.name
            )
        )
        //设置文件名
        binding.title.text = file.name
        //文件夹隐藏选中
        binding.checkLayout.visibility = if (file.isFile) View.VISIBLE else View.GONE
        if (file.isFile) binding!!.checkbox.isChecked = containsFile(file)

        binding.rootLayout.setOnClickListener {
            if (file.isDirectory) {
                if (mListener != null) mListener!!.onDirectoryItemClick(file)
            } else {
                processFileClick(file, binding)
            }
        }

    }

    private val selectFileList = ArrayList<File>()
    private fun processFileClick(file: File, binding: ItemFileBinding) {

        if (!binding!!.checkbox.isChecked && !containsFile(file)) {

            if (selectFileList.size < maxNum){
                if (FilesUtil.checkFileSize(mContext, file.length(), file.name)) {
                    binding!!.checkbox.isChecked = true
                    selectFileList.add(file)
                    if (mListener != null) mListener!!.onFileItemClick(selectFileList)
                }
            }else{

                Toast.makeText(
                    mContext, String.format("最多只能选择%s个文件", maxNum), Toast.LENGTH_SHORT
                ).show()

            }

        } else {
            binding!!.checkbox.isChecked = false
            selectFileList.remove(file)
            if (mListener != null) mListener!!.onFileItemClick(selectFileList)
        }

    }

    //选中集合是否包含此File
    private fun containsFile(curFile: File): Boolean {
        if (curFile.isDirectory) return false
        for (file in selectFileList) {
            if (file.absolutePath == curFile.absolutePath) {
                return true
            }
        }
        return false
    }


    private var mListener: OnFileItemSelectListener? = null

    fun setOnItemClickListener(listener: OnFileItemSelectListener) {
        mListener = listener
    }

    interface OnFileItemSelectListener {
        fun onDirectoryItemClick(file: File?)
        fun onFileItemClick(selects: ArrayList<File>?)
    }

}