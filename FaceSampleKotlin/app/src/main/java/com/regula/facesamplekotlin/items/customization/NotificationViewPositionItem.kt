package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.items.fragments.NotificationViewPositionFragment
import com.regula.facesamplekotlin.util.LivenessResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.model.results.LivenessResponse

class NotificationViewPositionItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = LivenessConfiguration.Builder()
            .registerUiFragmentClass(NotificationViewPositionFragment::class.java)
            .build()
        FaceSDK.Instance().startLiveness(
            context, configuration
        ) { livenessResponse: LivenessResponse -> LivenessResponseUtil.response(context, livenessResponse) }
    }

    override val title: String
        get() = "Notification text view position"

    override val description: String
        get() = "Change notification view position using default UI"
}