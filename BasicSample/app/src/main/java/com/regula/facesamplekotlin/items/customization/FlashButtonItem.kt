package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.items.fragments.FlashButtonFragment
import com.regula.facesamplekotlin.util.FaceCaptureResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.model.results.FaceCaptureResponse
import com.regula.facesdk.model.results.LivenessResponse

class FlashButtonItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = FaceCaptureConfiguration.Builder()
            .registerUiFragmentClass(FlashButtonFragment::class.java)
            .setTorchButtonEnabled(true)
            .build()
        FaceSDK.Instance().presentFaceCaptureActivity(
            context, configuration
        ) { faceCaptureResponse: FaceCaptureResponse -> FaceCaptureResponseUtil.response(context, faceCaptureResponse) }
    }

    override val title: String
        get() = "Custom flash button"

    override val description: String
        get() = "Change flash button using default UI"
}