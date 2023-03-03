package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.items.fragments.LivenessProcessingCustomFragment
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.model.results.LivenessResponse

class LivenessProcessingCustomItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = LivenessConfiguration.Builder()
            .registerProcessingFragment(LivenessProcessingCustomFragment::class.java)
            .build()
        FaceSDK.Instance().startLiveness(
            context, configuration
        ) { livenessResponse: LivenessResponse? -> }
    }

    override val title: String
        get() = "Custom liveness process"

    override val description: String
        get() = "Customize liveness processing and retry screens"
}