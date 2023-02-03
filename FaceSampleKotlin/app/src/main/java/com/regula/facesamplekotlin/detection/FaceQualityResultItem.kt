package com.regula.facesamplekotlin.detection

import com.regula.facesdk.detection.response.ImageQualityResult
import com.regula.facesdk.enums.ImageQualityResultStatus


class FaceQualityResultItem(private val result : ImageQualityResult?, private val image: String?) :
    IFaceQualityItem {


    constructor(result: ImageQualityResult) : this(
        result,
        null
    )

    override fun getTitle(): String {
        var status = "➖"
        if(result?.status == ImageQualityResultStatus.IMAGE_QUALITY_RESULT_STATUS_TRUE) {
            status = "✅ "
        } else if(result?.status == ImageQualityResultStatus.IMAGE_QUALITY_RESULT_STATUS_FALSE){
            status = "❌ "
        }
        return status + result?.name?.value
    }

    fun getDescription(): String {
        val value = "Value: " + result?.value
        val expectedRange = "\nExpected range: [" + result?.range?.min + " : " + result?.range?.max + "]"
        return value + expectedRange
    }

    override fun getImage(): String? {
        return image
    }

    override fun isHeader(): Boolean {
        return false
    }
}