package com.regula.facesamplekotlin.fragments

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.regula.facesamplekotlin.R
import com.regula.facesdk.fragment.FaceDefaultUiFragment

class NotificationViewFragment : FaceDefaultUiFragment() {

    override fun getNotificationBackgroundDrawable(
        context: Context?,
        isLightOn: Boolean
    ): Drawable? {
        return context?.let {
            ContextCompat.getDrawable(
                it, if (isLightOn) R.drawable.notification_view_background_dark else R.drawable.notification_view_background_white
            )
        }
    }

    override fun getNotificationTextColor(isLightOn: Boolean): Int {
        return ResourcesCompat.getColor(resources, if (isLightOn) R.color.notification_view_text_color_light_on else R.color.notification_view_text_color_light_off, null)
    }
}