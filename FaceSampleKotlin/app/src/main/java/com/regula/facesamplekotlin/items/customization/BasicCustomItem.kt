package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.items.fragments.BasicCustomUiFragment
import com.regula.facesamplekotlin.util.LivenessResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.model.results.LivenessResponse

class BasicCustomItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = LivenessConfiguration.Builder()
            .registerUiFragmentClass(BasicCustomUiFragment::class.java)
            .build()
        FaceSDK.Instance().startLiveness(
            context, configuration
        ) { livenessResponse: LivenessResponse -> LivenessResponseUtil.response(context, livenessResponse) }
    }

    override val title: String
        get() = "Basic custom UI fragment"

    override val description: String
        get() = "Custom UI with basic functionality"
}