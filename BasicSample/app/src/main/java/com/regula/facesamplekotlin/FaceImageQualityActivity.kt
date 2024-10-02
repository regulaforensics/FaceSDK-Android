package com.regula.facesamplekotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.regula.facesamplekotlin.detection.fragment.FaceImageQualityFragment
import com.regula.facesdk.detection.response.DetectFacesResponse

class FaceImageQualityActivity : AppCompatActivity() {

    lateinit var response: DetectFacesResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_quality)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,  FaceImageQualityFragment())
                .commitNow()
        }
    }
}