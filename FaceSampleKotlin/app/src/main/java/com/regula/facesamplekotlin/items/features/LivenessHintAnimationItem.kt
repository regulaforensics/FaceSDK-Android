package com.regula.facesamplekotlin.items.features

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.model.results.LivenessResponse

class LivenessHintAnimationItem : CategoryItem() {
    override fun onItemSelected(context: Context) {
        val configuration = LivenessConfiguration.Builder()
            .setShowHelpTextAnimation(false)
            .build()
        FaceSDK.Instance().startLiveness(
            context, configuration
        ) { livenessResponse: LivenessResponse? -> }
    }

    override val title: String
        get() = "Disable Liveness NotificationTextView animation"

    override val description: String
        get() = "Disables blinking NotificationTextView animation for Liveness."
}