package com.regula.facesamplekotlin.detection

import com.regula.facesdk.enums.ImageQualityGroupName

class GroupQualityResultItem(
    val id: Int,
    val compliantCount: Int,
    val totalCount: Int,
) : IFaceQualityItem {

    override fun getImage(): String? {
        return null
    }

    override fun getTitle(): String {
        var status = " ✅ "
        if (totalCount != compliantCount) {
            status = " ❌ "
        }

        return getGroupName() + status + "$compliantCount/$totalCount"
    }

    override fun isHeader(): Boolean {
        return true
    }

    private fun getGroupName(): String {
        when (id) {
            ImageQualityGroupName.IMAGE_CHARACTERISTICS.value -> return "Image characteristics"
            ImageQualityGroupName.HEAD_SIZE_AND_POSITION.value -> return "Head size and position"
            ImageQualityGroupName.FACE_QUALITY.value -> return "Face quality"
            ImageQualityGroupName.EYES_CHARACTERISTICS.value -> return "Eyes characteristics"
            ImageQualityGroupName.SHADOWS_AND_LIGHTNING.value -> return "Shadows and lightning"
            ImageQualityGroupName.POSE_AND_EXPRESSION.value -> return "Pose and expression"
            ImageQualityGroupName.HEAD_OCCLUSION.value -> return "Head occlusion"
            ImageQualityGroupName.BACKGROUND.value -> return "Background"
        }
        return "unknown"
    }

}