package com.regula.facesamplekotlin.items.features

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK

class FaceSdkDeinitItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        FaceSDK.Instance().deinit()
    }

    override val title: String
        get() = "Deinitialization SDK"

    override val description: String
        get() = "Deinitialization FaceSDK resources"
}