package com.regula.facesamplekotlin.items.features

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.exception.InitException

class FaceSdkInitItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        FaceSDK.Instance().init(
            context
        ) { status: Boolean, exception: InitException? -> }
    }

    override val title: String
        get() = "Initialization SDK"

    override val description: String
        get() = "Initialization FaceSDK resources"
}