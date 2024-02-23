package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.util.FaceCaptureResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.model.results.FaceCaptureResponse

class OverlayCustomItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        // uncomment in res/values/colors.xml:
        // all
        // facesdk_overlay_background_dark
        // facesdk_overlay_background_white
        // facesdk_overlay_border_default
        // facesdk_overlay_border_active
        FaceSDK.Instance().presentFaceCaptureActivity(
            context
        ) { faceCaptureResponse: FaceCaptureResponse -> FaceCaptureResponseUtil.response(context, faceCaptureResponse) }
    }

    override val title: String
        get() = "Custom Overlay"

    override val description: String
        get() = "Change colors for overlay"
}