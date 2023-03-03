package com.regula.facesamplekotlin.items.features

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.enums.LivenessSkipStep
import com.regula.facesdk.model.results.LivenessResponse

class LivenessDisableStepItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = LivenessConfiguration.Builder()
            .setSkipStep(LivenessSkipStep.START_STEP, LivenessSkipStep.DONE_STEP)
            .build()
        FaceSDK.Instance().startLiveness(
            context, configuration
        ) { livenessResponse: LivenessResponse? -> }
    }

    override val title: String
        get() = "Disable steps in liveness"

    override val description: String
        get() = "Hide some steps in liveness process"
}