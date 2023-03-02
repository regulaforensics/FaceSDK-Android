package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.items.fragments.NotificationViewFragment
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.model.results.FaceCaptureResponse

class NotificationViewItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = FaceCaptureConfiguration.Builder()
            .registerUiFragmentClass(NotificationViewFragment::class.java)
            .setCameraSwitchEnabled(true)
            .build()
        FaceSDK.Instance().presentFaceCaptureActivity(
            context, configuration
        ) { faceCaptureResponse: FaceCaptureResponse? -> }
    }

    override val title: String
        get() = "Custom notification background"

    override val description: String
        get() = "Custom notification background drawable based on Default UI"
}