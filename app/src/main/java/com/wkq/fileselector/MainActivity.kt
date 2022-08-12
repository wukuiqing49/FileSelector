package com.wkq.fileselector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
            //查询所有
            FileSelector.builder()
                    .bulid()
                    .startActivity(this, 10010,10011)
        }
        binding.btFileType.setOnClickListener {
            //按类型查询
            var types= arrayListOf<String>(".apk",".jpeg")
            FileSelector.builder()
                    .setFileType(types)
                    .setMaxNum(2).bulid()
                    .startActivity(this, 10010,10011)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10010&&resultCode==10011) {
            var data = data!!.getSerializableExtra(FilePickerResult.RESULT) as FilePickerResult
            Log.e("选中个数:", data.fileList.size.toString())
            if (data.fileList.size>0){
                Toast.makeText(this,data.fileList.get(0).name,Toast.LENGTH_LONG).show()
            }
        }
    }
}