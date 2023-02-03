package com.regula.facesamplekotlin.items.other

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.model.results.LivenessResponse

class LocalizationItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        // uncomment in res/values/strings.xml:
        // hint.fit resource
        FaceSDK.Instance().startLiveness(context) { livenessResponse: LivenessResponse? -> }
    }

    override val title: String
        get() =  "Custom localization"

    override val description: String
        get() =  "Localization hook example."
}