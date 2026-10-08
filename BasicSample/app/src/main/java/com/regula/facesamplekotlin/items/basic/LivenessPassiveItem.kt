package com.regula.facesamplekotlin.items.basic

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.util.LivenessResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.enums.LivenessType
import com.regula.facesdk.model.results.LivenessResponse

class LivenessPassiveItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = LivenessConfiguration.Builder()
            .setType(LivenessType.PASSIVE)
            .build()
        FaceSDK.Instance().startLiveness(
            context, configuration
        ) { livenessResponse: LivenessResponse -> LivenessResponseUtil.response(context, livenessResponse) }
    }

    override val title: String
        get() =  "Liveness – Passive"

    override val description: String
        get() = "Detects if a person on camera is alive"
}