package com.wkq.fileselector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.wkq.file.FileSelectorActivity
import com.wkq.file.util.FilePickerResult
import com.wkq.file.util.FileSelector
import com.wkq.fileselector.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.btFile.setOnClickListener {
            FileSelector.builder().bulid().startActivity(this, 10010,10011)
        }
        binding.btFileType.setOnClickListener {
            FileSelector.builder().setFileType(".apk").setMaxNum(2).bulid().startActivity(this, 10010,10011)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10010&&resultCode==10011) {
            var data = data!!.getSerializableExtra(FilePickerResult.RESULT) as FilePickerResult
            Log.e("选中个数:", data.fileList.size.toString())
        }
    }
}