package com.friend.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.friend.sample.ExtensionFun.getFileSize
import com.friend.sample.databinding.ActivityMain2Binding
import com.friend.sample.databinding.StoragevideoshowadaptercusotmBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity2 : AppCompatActivity() {

    lateinit var binding:ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title=intent?.getStringExtra("parent")
        binding.rec.adapter=VideoShowAdapder2(Common.listOfFile)
    }


    inner class VideoShowAdapder2(var listOfData: java.util.ArrayList<String>?) :
        RecyclerView.Adapter<VideoShowAdapder2.MyHolder>() {

        inner class MyHolder(var view: StoragevideoshowadaptercusotmBinding) :
            RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val binding =
                StoragevideoshowadaptercusotmBinding.inflate(layoutInflater, parent, false)



            return MyHolder(binding)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {

            Glide.with(this@MainActivity2).load(listOfData?.get(holder.adapterPosition))
                .placeholder(R.drawable.imageplaceholder).into(holder.view.imageView2)
            CoroutineScope(Dispatchers.Default).launch {
                val file = File(listOfData?.get(holder.adapterPosition))
                val size = file.length().getFileSize()
                withContext(Dispatchers.Main) {
                    holder.view.textView2.text = size
                }
            }




        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount(): Int {
            return listOfData?.size?:0
        }
    }
}