package com.regula.facesamplekotlin.items.basic

import android.content.Context
import android.content.Intent
import com.regula.facesamplekotlin.FaceImageQualityActivity
import com.regula.facesamplekotlin.category.CategoryItem

class FaceImageQualityRequestItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val intent = Intent(context, FaceImageQualityActivity::class.java)
        context.startActivity(intent)
    }

    override val title: String
        get() = "Face Image Quality"

    override val description: String
        get() = "Checks whether a photo portrait meets certain standards"
}