package com.regula.facesamplekotlin.items.basic

import android.content.Context
import android.content.Intent
import com.regula.facesamplekotlin.DetectFacesActivity
import com.regula.facesamplekotlin.category.CategoryItem

class DetectFacesRequestItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val intent = Intent(context, DetectFacesActivity::class.java)
        context.startActivity(intent)
    }

    override val title: String
        get() = "Detect Faces"

    override val description: String
        get() = "Analyzes images, recognizes faces in them, and returns cropped and aligned portraits of the detected people"
}