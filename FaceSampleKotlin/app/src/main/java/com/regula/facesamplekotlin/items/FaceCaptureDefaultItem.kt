package com.regula.facesamplekotlin.items

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration

class FaceCaptureDefaultItem(private val title: Int, override val description: Int) : CategoryItem() {

    private lateinit var configuration: FaceCaptureConfiguration

    constructor(title: Int, description: Int, configuration: FaceCaptureConfiguration) : this(
        title,
        description
    ) {
        this.configuration = configuration
    }

    override fun onItemSelected(context: Context) {
        if (::configuration.isInitialized) {
            FaceSDK.Instance().presentFaceCaptureActivity(context, configuration) { response ->
                // ... check response.image for capture result
            }
        } else {
            FaceSDK.Instance().presentFaceCaptureActivity(context) { response ->
                // ... check response.image for capture result
            }
        }
    }

    override fun getTitle(): Int {
        return title
    }
}