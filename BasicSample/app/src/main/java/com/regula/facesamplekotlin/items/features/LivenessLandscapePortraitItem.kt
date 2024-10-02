package com.regula.facesamplekotlin.items.features

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.util.LivenessResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.enums.ScreenOrientation
import com.regula.facesdk.model.results.LivenessResponse

class LivenessLandscapePortraitItem(p: Boolean, l: Boolean) : CategoryItem() {

    private val portrait = p
    private val landscape = l
    override fun onItemSelected(context: Context) {

        var screenOrientation = arrayOf(ScreenOrientation.PORTRAIT)
        if (portrait && landscape) {
            screenOrientation = arrayOf(ScreenOrientation.PORTRAIT, ScreenOrientation.LANDSCAPE)
        } else if (landscape) {
            screenOrientation = arrayOf(ScreenOrientation.LANDSCAPE)
        }
        val configuration = LivenessConfiguration.Builder()
            .setScreenOrientation(*screenOrientation)
            .build()
        FaceSDK.Instance().startLiveness(
            context, configuration
        ) { livenessResponse: LivenessResponse ->
            LivenessResponseUtil.response(
                context,
                livenessResponse
            )
        }
    }

    override val title: String
        get() = if (portrait && landscape) "Liveness Portrait and Landscape" else {
            if (landscape) "Liveness Landscape" else "Liveness Portrait"
        }


    override val description: String
        get() = if (portrait && landscape) "Screen orientation depends on device rotation" else {
            if (landscape) "Liveness process in landscape orientation" else "Liveness process in portrait orientation"
        }
}