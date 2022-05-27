package com.yj510.gallery_0527

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.yj510.gallery_0527.databinding.ActivityMainBinding
import com.yj510.gallery_0527.databinding.DialogBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val path = File("${filesDir}/image/default")
        if(!path.exists()){
            path.mkdirs()
        }else{
            Toast.makeText(this, "기본 폴더가 이미 존재합니다.",Toast.LENGTH_SHORT).show()
        }
        binding.btnDirList.setOnClickListener {
            showDirectoryList()
        }

        binding.btnMkdir.setOnClickListener {
            insertDirectoryName()
        }

        binding.btnRmdir.setOnClickListener {
            deleteDirectory()
        }

    }

    private fun showDirectoryList(){
        binding.textView.text = null

        val path = File("${filesDir}/image")
        val files = path.listFiles()
        var strFileList: String? = "<디렉토리 목록>\n"
        for(file in files){
            strFileList += file.name + "\n"
        }
        binding.textView.text = strFileList
    }

    private fun insertDirectoryName(){
        val alertDialogEditText = DialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("새 앨범 생성")
            .setView(alertDialogEditText.root)
            .setPositiveButton("OK"){
                    dialogInterface: DialogInterface, i: Int ->
                makeDirectory(alertDialogEditText.editText.text.toString())
            }.show()
    }

    private fun makeDirectory(folderName:String){

        if(folderName.length==0) {
            Toast.makeText(this,"이름을 지정해주세요",Toast.LENGTH_SHORT).show()
            return
        }

        val path = File("$filesDir/image/$folderName")
        if(!path.exists()){
            path.mkdirs()
        }
        else{
           Toast.makeText(this,"$folderName is exists",Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteDirectory(){
        val files = File("${filesDir}/image").listFiles()
        val selectedItemIndex = ArrayList<Int>()
        var filesName = Array(files.size) { item -> "" }
        for(i in files.indices){
            filesName[i] = files[i].name
        }
        val builder = AlertDialog.Builder(this)
            .setTitle("삭제할 디렉토리 선택")
            .setMultiChoiceItems(filesName, null){
                    dialogInterface: DialogInterface, i: Int, b: Boolean ->
                if(b){
                    selectedItemIndex.add(i)            }
                else if(selectedItemIndex.contains(i)){
                    selectedItemIndex.remove(i)
                }
            }
            .setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                for(i in selectedItemIndex.indices){
                    val file = File("$filesDir/image/${filesName[selectedItemIndex[i]]
                    }")
                    file.delete()
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

}