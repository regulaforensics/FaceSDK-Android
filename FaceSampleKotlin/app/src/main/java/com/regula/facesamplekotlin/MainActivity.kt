package com.regula.facesamplekotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.regula.facesamplekotlin.CategoryDataProvider.Companion.getCategoryData
import com.regula.facesamplekotlin.databinding.ActivityStartBinding

class MainActivity : AppCompatActivity() {

    @Transient
    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CategoryAdapter(this, getCategoryData())
    }
}