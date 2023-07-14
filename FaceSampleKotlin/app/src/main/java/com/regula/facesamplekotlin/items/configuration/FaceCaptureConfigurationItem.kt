package com.regula.facesamplekotlin.items.configuration

import android.content.Context
import com.regula.facesamplekotlin.R
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.configuration.UiConfiguration
import com.regula.facesdk.enums.CustomizationColor
import com.regula.facesdk.enums.CustomizationFont
import com.regula.facesdk.enums.CustomizationImage

class FaceCaptureConfigurationItem : CategoryItem() {

    override fun onItemSelected(context: Context) {

        val uiConfiguration = UiConfiguration.Builder()

            //Configuration camera screen
            .setColor(CustomizationColor.CAMERA_SCREEN_BACK_HINT_LABEL_TEXT, R.color.title_color)
            .setColor(CustomizationColor.CAMERA_SCREEN_BACK_HINT_LABEL_BACKGROUND, R.color.light_gray)

            .setColor(CustomizationColor.CAMERA_SCREEN_FRONT_HINT_LABEL_TEXT, R.color.title_color)
            .setColor(CustomizationColor.CAMERA_SCREEN_FRONT_HINT_LABEL_BACKGROUND, R.color.light_gray)

            .setColor(CustomizationColor.CAMERA_SCREEN_LIGHT_TOOLBAR_TINT, R.color.title_color)
            .setColor(CustomizationColor.CAMERA_SCREEN_DARK_TOOLBAR_TINT, R.color.light_gray)

            .setColor(CustomizationColor.CAMERA_SCREEN_STROKE_ACTIVE, R.color.light_gray)
            .setColor(CustomizationColor.CAMERA_SCREEN_STROKE_NORMAL, R.color.image_color)

            .setImage(CustomizationImage.CAMERA_SCREEN_CLOSE_BUTTON, R.drawable.close_button)
            .setImage(CustomizationImage.CAMERA_SCREEN_SWITCH_BUTTON, R.drawable.swap_button)
            .setImage(CustomizationImage.CAMERA_SCREEN_LIGHT_OFF_BUTTON, R.drawable.flash_light_off)
            .setImage(CustomizationImage.CAMERA_SCREEN_LIGHT_ON_BUTTON, R.drawable.flash_light_on)

            .setFont(CustomizationFont.CAMERA_SCREEN_HINT_LABEL, R.font.roboto)

            //Configuration processing screen
            .setColor(CustomizationColor.PROCESSING_SCREEN_BACKGROUND, R.color.light_gray)
            .setColor(CustomizationColor.PROCESSING_SCREEN_PROGRESS, R.color.title_color)
            .setColor(CustomizationColor.PROCESSING_SCREEN_PROGRESS, R.color.message_color)
            .setFont(CustomizationFont.PROCESSING_SCREEN, R.font.roboto)

            .build()

        FaceSDK.Instance().customization.setUiConfiguration(uiConfiguration)

        val configuration = FaceCaptureConfiguration.Builder()
            .setCameraSwitchEnabled(true)
            .setCameraId(0)
            .build()
        FaceSDK.Instance().presentFaceCaptureActivity(
            context, configuration
        ) {
            FaceSDK.Instance().customization.setUiConfiguration(UiConfiguration.Builder().build())
        }
    }

    override val title: String
        get() = "Face Capture Configuration"

    override val description: String
        get() = "Customization interface"
}