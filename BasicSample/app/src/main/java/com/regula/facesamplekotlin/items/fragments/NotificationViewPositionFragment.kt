package com.regula.facesamplekotlin.items.fragments

import android.graphics.RectF
import androidx.constraintlayout.widget.Guideline
import com.regula.facesamplekotlin.R
import com.regula.facesdk.fragment.FaceDefaultUiFragment

class NotificationViewPositionFragment : FaceDefaultUiFragment() {

    override fun onFaceAreaViewPositionUpdated(rect: RectF) {
        val view = view ?: return
        var topMargin = rect.top.toInt()
        topMargin += (rect.height() * 0.4).toInt()
        val notificationGuideline: Guideline = view.findViewById(R.id.topNotificationGuideline)
        notificationGuideline.setGuidelinePercent(-1f)
        notificationGuideline.setGuidelineBegin(topMargin)
    }
}