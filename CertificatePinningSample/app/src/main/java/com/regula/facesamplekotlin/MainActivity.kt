package com.regula.facesamplekotlin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        FaceSDK.Instance().init(this) { status: Boolean, e: InitException? ->
            if (!status) {
                Toast.makeText(
                    this@MainActivity,
                    "Init finished with error: " + if (e != null) e.message else "",
                    Toast.LENGTH_LONG
                ).show()
                return@init
            }
            (Handler(Looper.getMainLooper())).post {
                binding.startBtn.isEnabled = true
            }
            Log.d("MainActivity", "FaceSDK init completed successfully")
        }

        binding.startBtn.setOnClickListener {
            FaceSDK.Instance().startLiveness(this) {
                if (it.exception != null)
                    Toast.makeText(this, "Error: " + it.exception!!.message, Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Liveness status: " + it.liveness.name, Toast.LENGTH_SHORT).show()
            }
        }
    }
}