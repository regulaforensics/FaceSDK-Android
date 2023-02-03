package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.items.fragments.AdvancedCustomUiFragment
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.model.results.LivenessResponse

class AdvancedCustomItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = LivenessConfiguration.Builder()
            .registerUiFragmentClass(AdvancedCustomUiFragment::class.java)
            .build()
        FaceSDK.Instance().startLiveness(
            context, configuration
        ) { livenessResponse: LivenessResponse? -> }
    }

    override val title: String
        get() = "Liveness toolbar appearance"

    override val description: String
        get() = "Usage example for toolbar."
}