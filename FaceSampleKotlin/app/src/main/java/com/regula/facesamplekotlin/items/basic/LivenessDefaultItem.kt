package com.regula.facesamplekotlin.items.basic

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.model.results.LivenessResponse

class LivenessDefaultItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        FaceSDK.Instance().startLiveness(context) { livenessResponse: LivenessResponse? -> }
    }

    override val title: String
        get() =  "Liveness"

    override val description: String
        get() = "Detects if a person on camera is alive."
}