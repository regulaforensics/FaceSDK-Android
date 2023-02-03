package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.model.results.FaceCaptureResponse

class HideCloseButtonItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = FaceCaptureConfiguration.Builder()
            .setCloseButtonEnabled(false)
            .build()
        FaceSDK.Instance().presentFaceCaptureActivity(
            context, configuration
        ) { faceCaptureResponse: FaceCaptureResponse? -> }
    }

    override val title: String
        get() = "Hide close button"

    override val description: String
        get() = "Hide close button using default UI"
}