package com.regula.facesamplekotlin.items.features

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.util.LivenessResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.enums.LivenessSkipStep
import com.regula.facesdk.model.results.LivenessResponse

class LivenessDisableStepItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val configuration = LivenessConfiguration.Builder()
            .setSkipStep(LivenessSkipStep.ONBOARDING_STEP, LivenessSkipStep.SUCCESS_STEP)
            .build()
        FaceSDK.Instance().startLiveness(
            context, configuration
        ) { livenessResponse: LivenessResponse -> LivenessResponseUtil.response(context, livenessResponse) }
    }

    override val title: String
        get() = "Skip Onbording & Success steps"

    override val description: String
        get() = "Liveness will not show onboarding & success screens"
}