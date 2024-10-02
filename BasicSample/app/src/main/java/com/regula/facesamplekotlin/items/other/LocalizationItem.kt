package com.regula.facesamplekotlin.items.other

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.model.results.LivenessResponse

class LocalizationItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        // uncomment in res/values/strings.xml:
        // hint.fit resource

        //This is another approach of customizing localization by overriding strings in callback

        // uncomment in res/values/strings.xml:
        // hint.fit resource


        //This is another approach of customizing localization by overriding strings in callback
        FaceSDK.Instance().setLocalizationCallback { stringId ->
            if (stringId.equals("livenessGuide.head")) return@setLocalizationCallback "this is custom selfie time string string"
            null
        }

        FaceSDK.Instance().startLiveness(context) { livenessResponse: LivenessResponse? -> }
    }

    override val title: String
        get() =  "Custom localization"

    override val description: String
        get() =  "Localization hook example."
}