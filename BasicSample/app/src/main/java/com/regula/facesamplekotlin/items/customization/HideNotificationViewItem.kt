package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.items.fragments.HideNotificationViewFragment
import com.regula.facesamplekotlin.util.FaceCaptureResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.model.results.FaceCaptureResponse

class HideNotificationViewItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = FaceCaptureConfiguration.Builder()
            .setCameraSwitchEnabled(true)
            .registerUiFragmentClass(HideNotificationViewFragment::class.java)
            .build()
        FaceSDK.Instance().presentFaceCaptureActivity(
            context, configuration
        ) { faceCaptureResponse: FaceCaptureResponse -> FaceCaptureResponseUtil.response(context, faceCaptureResponse) }
    }

    override val title: String
        get() = "Hide notification text view"

    override val description: String
        get() = "Hide notification text view using default UI fragment"
}