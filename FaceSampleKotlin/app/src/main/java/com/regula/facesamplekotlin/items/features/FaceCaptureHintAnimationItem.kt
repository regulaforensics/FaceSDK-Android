package com.regula.facesamplekotlin.items.features

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.model.results.FaceCaptureResponse

class FaceCaptureHintAnimationItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = FaceCaptureConfiguration.Builder()
            .setShowHelpTextAnimation(false)
            .build()
        FaceSDK.Instance().presentFaceCaptureActivity(
            context, configuration
        ) { faceCaptureResponse: FaceCaptureResponse? -> }
    }

    override val title: String
        get() = "Disable FaceCapture NotificationTextView animation"

    override val description: String
        get() = "Disables blinking NotificationTextView animation for FaceCapture."
}