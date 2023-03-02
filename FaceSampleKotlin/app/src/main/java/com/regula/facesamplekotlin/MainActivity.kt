package com.regula.facesamplekotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.regula.facesamplekotlin.CategoryDataProvider.Companion.getCategoryData
import com.regula.facesamplekotlin.databinding.ActivityMainBinding
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.exception.InitException

class MainActivity : AppCompatActivity() {

    @Transient
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CategoryAdapter(this, getCategoryData())

        FaceSDK.Instance().init(this) { status: Boolean, e: InitException? ->
            if (!status) {
                Toast.makeText(
                    this@MainActivity,
                    "Init finished with error: " + if (e != null) e.message else "",
                    Toast.LENGTH_LONG
                ).show()
                return@init
            }
            Log.d("MainActivity", "FaceSDK init completed successfully")
        }
    }
}