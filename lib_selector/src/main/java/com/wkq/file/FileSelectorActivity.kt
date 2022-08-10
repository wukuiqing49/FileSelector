package com.wkq.file

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wkq.file.databinding.ActivityFileSelectorBinding
import com.wkq.file.util.*
import com.wkq.snaphelper.FileSelectorAdapter
import com.wu.base.util.AlertUtil
import com.wu.base.util.PermissionChecker
import com.wu.base.util.StatusBarUtil
import java.io.File
import java.util.*

class FileSelectorActivity : AppCompatActivity() {
    //读取权限的Code
    private val REQUEST_CODE_WRITE_STORAGE = 0x1001
    private val REQUEST_CODE_SYS_RXTRA_FILE = 0x1002

    var binding: ActivityFileSelectorBinding? = null

    //文件的适配器
    var mAdapter: FileSelectorAdapter? = null

    //当前的文件
    private var mCurrentDirectory: File? = null
    var selectFiles = ArrayList<File>()

    //类型
    var type = "All"

    //最大选中个数
    var maxCount = 0
    var resultCode = 10011
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityFileSelectorBinding>(
            this,
            R.layout.activity_file_selector
        )
        initView()
        if (initPermission()) {
            if (type.equals(Constant.TYPE)) {
                loadFile(getRootFile()!!)
            } else {
                processFileType()
            }
        }
    }


    private fun initView() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.color_282828), 0)
        var maxFileSize = intent.getLongExtra(Constant.EXTRA_MAX_FILE_SIZE, Constant.FILE_MAX_SIZE)
        var maxVideoSize =
            intent.getLongExtra(Constant.EXTRA_MAX_VIDEO_SIZE, Constant.VIDEO_MAX_SIZE)
        var maxImagSize =
            intent.getLongExtra(Constant.EXTRA_MAX_IMAGE_SIZE, Constant.IMAGE_MAX_SIZE)
        //类型
        type = intent.getStringExtra(Constant.EXTRA_SELECT_TYPE) ?: Constant.TYPE
        //最大选择个数
        maxCount = intent.getIntExtra(Constant.SELECT_MAX_FILE_COUNT, Constant.MAX_NUM)
        resultCode = intent.getIntExtra(Constant.EXTRA_RESULT_CODE, Constant.MAX_NUM)
        //设置最大文件限制

        FilesUtil.maxFileSize = maxFileSize
        FilesUtil.maxVideoSize = maxVideoSize
        FilesUtil.maxImageSize = maxImagSize

        mAdapter = FileSelectorAdapter(this)
        binding!!.rvContent.layoutManager = LinearLayoutManager(this)
        binding!!.rvContent.adapter = mAdapter
        binding!!.title.text = "手机存储"
        binding!!.close.setOnClickListener {
            onBackPressed()
        }
        topDirectory = getRootFile()!!.path
        mAdapter!!.setOnItemClickListener(object : FileSelectorAdapter.OnFileItemSelectListener {
            override fun onDirectoryItemClick(file: File?) {
                loadFile(file!!)
            }

            override fun onFileItemClick(selects: ArrayList<File>?) {
                var cur = selects!!.size
                binding!!.send.text = "(发送$cur/$maxCount)"
                selectFiles = selects
            }
        })
        binding!!.send.setOnClickListener {

            if (selectFiles == null || selectFiles.size <= 0) {
                AlertUtil.showDeftToast(this, getString(R.string.select_no_file))
                return@setOnClickListener
            }
            val intent = intent
            intent.putExtra(FilePickerResult.RESULT, FilePickerResult(selectFiles))
            setResult(resultCode, intent)
            finish()
        }
    }

    /**
     * 获取根目录的文件
     */
    private fun getRootFile(): File? {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            return Environment.getExternalStorageDirectory()
        }
        return Environment.getRootDirectory()
    }

    //最顶层的文件夹
    private var topDirectory: String? = null

    //加载文件
    private fun loadFile(rootFile: File) {
        mCurrentDirectory = rootFile
        var listFils = rootFile!!.listFiles(CustomFileTypeFileter())

        if (listFils == null || listFils.size == 0) {
            showEmpty(true)
            return
        } else {
            showEmpty(false)
        }
        var datas = listFils.toMutableList()

        try {

            Collections.sort<File>(datas, FilesListComparator())
            mAdapter!!.removeAllItems()
            mAdapter!!.addItems(datas)
            binding!!.path.text = String.format("%s 个文件", datas.size)
            binding!!.send.visibility = View.VISIBLE
        } catch (e: Exception) {
        }

    }

    /**
     * 处理类型文件
     */
    private fun processFileType() {

        var datas = FilePickerUtils.getFileByType(this, arrayOf(type))
        if (datas.size > 0) {
            showEmpty(false)
        } else {
            showEmpty(true)
            return
        }
        try {
            Collections.sort<File>(datas, FilesListComparator())
            mAdapter!!.removeAllItems()
            mAdapter!!.addItems(datas)
            binding!!.path.text = String.format("%s 个文件", datas.size)
            binding!!.send.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
    }

    /**
     * 展示空布局
     */
    fun showEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding!!.rvContent.visibility = View.GONE
            binding!!.emptyView.visibility = View.VISIBLE
        } else {
            binding!!.rvContent.visibility = View.VISIBLE
            binding!!.emptyView.visibility = View.GONE
        }

    }

    private fun initPermission(): Boolean {
        return PermissionChecker.checkPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_WRITE_STORAGE,
            R.string.permission_write_storage_checker,
            true
        )
    }


    private fun isTopDirectory(directory: File?): Boolean {
        return directory != null && topDirectory == directory.absolutePath
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
            val hasPermission = PermissionChecker.onRequestPermissionsResult(
                this,
                requestCode,
                permissions,
                grantResults,
                true,
                R.string.permission_write_storage_checker
            )[0]
            val isNeverAsk = PermissionChecker.onRequestPermissionsResult(
                this,
                requestCode,
                permissions,
                grantResults,
                true,
                R.string.permission_write_storage_checker
            )[1]
            if (!hasPermission && !isNeverAsk) {
                finish()
            } else if (hasPermission) {

                if (type.equals(Constant.TYPE)) {
                    loadFile(getRootFile()!!)
                } else {
                    processFileType()
                }
            }
        }

    }

    //处理
    override fun onBackPressed() {
        //自定义类型直接关闭
        if (!type.equals(Constant.TYPE)) {
            super.onBackPressed()
            return
        }
        if (mCurrentDirectory == null || isTopDirectory(mCurrentDirectory)) {
            super.onBackPressed()
        } else {
            loadFile(mCurrentDirectory!!.parentFile)
        }
    }

}