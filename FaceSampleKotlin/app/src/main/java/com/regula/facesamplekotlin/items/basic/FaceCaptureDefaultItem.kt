package com.regula.facesamplekotlin.items.basic

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK

class FaceCaptureDefaultItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        FaceSDK.Instance().presentFaceCaptureActivity(context) { response ->
            // ... check response.image for capture result
        }
    }

    override val title: String
        get() = "FaceCapture"

    override val description: String
        get() = "Automatically captures face photo."

}