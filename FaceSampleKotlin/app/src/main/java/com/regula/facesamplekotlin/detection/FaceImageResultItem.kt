package com.regula.facesamplekotlin.detection


class FaceImageResultItem(private val image: String?) :
    IFaceQualityItem {

    override fun getTitle(): String {
        return ""
    }

    override fun getImage(): String? {
        return image
    }

    override fun isHeader(): Boolean {
        return false
    }
}