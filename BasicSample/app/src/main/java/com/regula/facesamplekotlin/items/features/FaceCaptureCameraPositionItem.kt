package com.regula.facesamplekotlin.items.features

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.util.FaceCaptureResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.model.results.FaceCaptureResponse

class FaceCaptureCameraPositionItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = FaceCaptureConfiguration.Builder()
            .setCameraSwitchEnabled(true)
            .setCameraId(0)
            .build()
        FaceSDK.Instance().presentFaceCaptureActivity(
            context, configuration
        ) { faceCaptureResponse: FaceCaptureResponse -> FaceCaptureResponseUtil.response(context, faceCaptureResponse) }
    }

    override val title: String
        get() = "Face Capture camera position"

    override val description: String
        get() = "Changes default camera position to back"
}