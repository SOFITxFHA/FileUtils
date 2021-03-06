package com.friend.sample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment

import android.provider.Settings

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.file.fileutils.*
import com.file.fileutils.FileUtils.getFilesFromDownload
import com.friend.sample.ExtensionFun.getFileSize
import com.friend.sample.ExtensionFun.hide
import com.friend.sample.ExtensionFun.visible
import com.friend.sample.databinding.ActivityMainBinding
import com.friend.sample.databinding.AdaptercustomBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var type: UriType
    var askForReadWritePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                binding.rec.adapter = null
                binding.progressBar.visible()
                getFIles(type)
            }
        }
    var askForAllFileAccessPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClickEvent()


        FileUtils.getAvailableExternalMemorySize(object : GetStorageStateCallBack {
            override fun onSuccess(total: Long, available: Long) {
                binding.totalStr.text = total.getFileSize()
                binding.avaStorage.text = available.getFileSize()
            }

            override fun onFailure(ex: java.lang.Exception) {

            }
        })




    }

    override fun onResume() {
        super.onResume()
        if (!Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            askForAllFileAccessPermission.launch(intent)

        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                getFilesFromDownload(this@MainActivity,object : SpecificFolderFileCallback {
                    override fun onSuccess(data: ArrayList<String?>) {
                            data?.forEach {
                                Log.d("path " , it.toString())
                            }
                    }

                    override fun onFailure(ex: java.lang.Exception) {

                    }
                })
            }
        }
    }

    fun onClickEvent() {
        binding.getImg.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                binding.rec.adapter = null
                binding.progressBar.visible()
                getFIles(UriType.IMAGE_URL)
            } else {
                type = UriType.IMAGE_URL
                askForReadWritePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }

        binding.getAudio.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                binding.rec.adapter = null
                binding.progressBar.visible()
                getFIles(UriType.AUDIO_URL)
            } else {
                type = UriType.AUDIO_URL
                askForReadWritePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }

        binding.getVideo.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                binding.rec.adapter = null
                binding.progressBar.visible()
                getFIles(UriType.VIDEO_URL)
            } else {
                type = UriType.VIDEO_URL
                askForReadWritePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }



        binding.getAllAudios.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {


                binding.progressBar.visible()
                binding.rec.adapter = null
                FileUtils.getFiles(this, UriType.AUDIO_URL, object : Callback {
                    override fun onSuccess(data: ArrayList<String>) {
                        binding.progressBar.hide()
                        Common.listOfFile = data
                        startActivity(
                            Intent(this@MainActivity, MainActivity2::class.java).putExtra(
                                "parent",
                                "Audio"
                            )
                        )
                    }

                    override fun onSuccess(isSuccess: Boolean) {

                    }

                    override fun onFailure(ex: java.lang.Exception) {
                        binding.progressBar.hide()
                        Toast.makeText(this@MainActivity, ex.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                askForReadWritePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        binding.getAllImgs.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {


                binding.progressBar.visible()
                binding.rec.adapter = null
                FileUtils.getFiles(this, UriType.IMAGE_URL, object : Callback {
                    override fun onSuccess(data: ArrayList<String>) {
                        data.forEach {
                            Log.d("data", it)
                        }
                        binding.progressBar.hide()
                        Common.listOfFile = data
                        startActivity(
                            Intent(this@MainActivity, MainActivity2::class.java).putExtra(
                                "parent",
                                "Images"
                            )
                        )
                    }

                    override fun onSuccess(isSuccess: Boolean) {

                    }

                    override fun onFailure(ex: java.lang.Exception) {
                        binding.progressBar.hide()
                        Toast.makeText(this@MainActivity, ex.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                askForReadWritePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        binding.getAllVideos.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                binding.progressBar.visible()
                binding.rec.adapter = null
                FileUtils.getFiles(this, UriType.VIDEO_URL, object : Callback {
                    override fun onSuccess(data: ArrayList<String>) {
                        binding.progressBar.hide()
                        Common.listOfFile = data
                        startActivity(
                            Intent(this@MainActivity, MainActivity2::class.java).putExtra(
                                "parent",
                                "Videos"
                            )
                        )
                    }

                    override fun onSuccess(isSuccess: Boolean) {

                    }

                    override fun onFailure(ex: java.lang.Exception) {
                        binding.progressBar.hide()
                        Toast.makeText(this@MainActivity, ex.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                askForReadWritePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    fun getFIles(type: UriType) {
        FileUtils.getGroupedFolderFiles(
            Environment.getExternalStorageDirectory(),
            type,
            object : FileGroupedCallback {

                override fun onSuccess(data: HashMap<String, ArrayList<String>?>) {
                    binding.progressBar.hide()
                    data.forEach { parent ->
                        Log.d("data", "---------------------------------------------------------")
                        Log.d("data", "Parent is ${parent.key}")
                        parent.value?.forEach {
                            Log.d("data", "child is $it")
                        }
                    }

                    binding.rec.adapter = VideoShowAdapder1(data)
                }

                override fun onFailure(ex: Exception) {
                    binding.progressBar.hide()
                    Log.d("error", ex.message.toString())
                }
            })
    }


    inner class VideoShowAdapder1(var listOfData: HashMap<String, ArrayList<String>?>) :
        RecyclerView.Adapter<VideoShowAdapder1.MyHolder>() {

        inner class MyHolder(var view: AdaptercustomBinding) :
            RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val binding =
                AdaptercustomBinding.inflate(layoutInflater, parent, false)



            return MyHolder(binding)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            val list = listOfData.keys.toTypedArray()

            holder.view.textView.text = list[holder.adapterPosition]
            holder.view.cardView.setOnClickListener {
                Common.listOfFile = listOfData[list[holder.adapterPosition]]
                startActivity(
                    Intent(
                        this@MainActivity,
                        MainActivity2::class.java
                    ).putExtra("parent", list[holder.adapterPosition])
                )
            }
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount(): Int {
            return listOfData.size
        }
    }


}