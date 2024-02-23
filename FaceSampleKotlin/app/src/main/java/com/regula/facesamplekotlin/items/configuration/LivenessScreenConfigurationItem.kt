package com.regula.facesamplekotlin.items.configuration

import android.content.Context
import com.regula.facesamplekotlin.R
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.UiConfiguration
import com.regula.facesdk.enums.CustomizationColor
import com.regula.facesdk.enums.CustomizationFont
import com.regula.facesdk.enums.CustomizationImage

class LivenessScreenConfigurationItem : CategoryItem() {

    override fun onItemSelected(context: Context) {

        val uiConfiguration = UiConfiguration.Builder()

            //Configuration onboarding screen
            .setColor(CustomizationColor.ONBOARDING_SCREEN_BACKGROUND, R.color.background_color)
            .setColor(CustomizationColor.ONBOARDING_SCREEN_START_BUTTON_BACKGROUND, R.drawable.custom_button_drawable)
            .setColor(CustomizationColor.ONBOARDING_SCREEN_START_BUTTON_TITLE, R.color.background_color)
            .setColor(CustomizationColor.ONBOARDING_SCREEN_TITLE_LABEL_TEXT, R.color.title_color)
            .setColor(CustomizationColor.ONBOARDING_SCREEN_SUBTITLE_LABEL_TEXT, R.color.light_gray)
            .setColor(CustomizationColor.ONBOARDING_SCREEN_MESSAGE_LABELS_TEXT, R.color.message_color)
            .setImage(CustomizationImage.ONBOARDING_SCREEN_CLOSE_BUTTON, R.drawable.close_button)

            .setImage(CustomizationImage.ONBOARDING_SCREEN_ILLUMINATION, R.drawable.ic_looks_1)
            .setImage(CustomizationImage.ONBOARDING_SCREEN_ACCESSORIES, R.drawable.ic_looks_2)
            .setImage(CustomizationImage.ONBOARDING_SCREEN_CAMERA_LEVEL, R.drawable.ic_looks_3)

            .setFont(CustomizationFont.ONBOARDING_SCREEN_TITLE_LABEL, R.font.roboto_italic)
            .setFont(CustomizationFont.ONBOARDING_SCREEN_SUBTITLE_LABEL, R.font.roboto_italic)
            .setFont(CustomizationFont.ONBOARDING_SCREEN_MESSAGE_LABELS, R.font.roboto)
            .setFont(CustomizationFont.ONBOARDING_SCREEN_START_BUTTON, R.font.roboto)

            .setFontSize(CustomizationFont.ONBOARDING_SCREEN_TITLE_LABEL, 35)
            .setFontSize(CustomizationFont.ONBOARDING_SCREEN_SUBTITLE_LABEL, 30)
            .setFontSize(CustomizationFont.ONBOARDING_SCREEN_MESSAGE_LABELS, 25)
            .setFontSize(CustomizationFont.ONBOARDING_SCREEN_START_BUTTON, 40)


            //Configuration camera screen
            .setColor(CustomizationColor.CAMERA_SCREEN_FRONT_HINT_LABEL_TEXT, R.color.title_color)
            .setColor(CustomizationColor.CAMERA_SCREEN_FRONT_HINT_LABEL_BACKGROUND, R.drawable.notification_background_white)
            .setColor(CustomizationColor.CAMERA_SCREEN_LIGHT_TOOLBAR_TINT, R.color.title_color)

            .setColor(CustomizationColor.CAMERA_SCREEN_SECTOR_ACTIVE, R.color.light_gray)
            .setColor(CustomizationColor.CAMERA_SCREEN_SECTOR_TARGET, R.color.title_color)
            .setColor(CustomizationColor.CAMERA_SCREEN_STROKE_ACTIVE, R.color.light_gray)
            .setColor(CustomizationColor.CAMERA_SCREEN_STROKE_NORMAL, R.color.image_color)

            .setImage(CustomizationImage.CAMERA_SCREEN_CLOSE_BUTTON, R.drawable.close_button)

            .setFont(CustomizationFont.CAMERA_SCREEN_HINT_LABEL, R.font.roboto)
            .setFontSize(CustomizationFont.RETRY_SCREEN_TITLE_LABEL, 35)


            //Configuration processing screen
            .setColor(CustomizationColor.PROCESSING_SCREEN_BACKGROUND, R.color.light_gray)
            .setColor(CustomizationColor.PROCESSING_SCREEN_PROGRESS, R.color.title_color)
            .setColor(CustomizationColor.PROCESSING_SCREEN_PROGRESS, R.color.message_color)
            .setFont(CustomizationFont.PROCESSING_SCREEN, R.font.roboto)
            .setFontSize(CustomizationFont.PROCESSING_SCREEN, 30)


            //Configuration retry screen
            .setImage(CustomizationImage.RETRY_SCREEN_CLOSE_BUTTON, R.drawable.close_button)
            .setImage(CustomizationImage.RETRY_SCREEN_HINT_ENVIRONMENT, R.drawable.ic_light)
            .setImage(CustomizationImage.RETRY_SCREEN_HINT_SUBJECT, R.drawable.ic_emotions)

            .setColor(CustomizationColor.RETRY_SCREEN_BACKGROUND, R.color.background_color)
            .setColor(CustomizationColor.RETRY_SCREEN_TITLE_LABEL_TEXT, R.color.title_color)
            .setColor(CustomizationColor.RETRY_SCREEN_SUBTITLE_LABEL_TEXT, R.color.light_gray)
            .setColor(CustomizationColor.RETRY_SCREEN_HINT_LABELS_TEXT, R.color.message_color)
            .setColor(CustomizationColor.RETRY_SCREEN_RETRY_BUTTON_BACKGROUND, R.color.title_color)
            .setColor(CustomizationColor.RETRY_SCREEN_RETRY_BUTTON_TITLE, R.color.image_color)

            .setFont(CustomizationFont.RETRY_SCREEN_TITLE_LABEL, R.font.roboto)
            .setFont(CustomizationFont.RETRY_SCREEN_SUBTITLE_LABEL, R.font.roboto_italic)
            .setFont(CustomizationFont.RETRY_SCREEN_HINT_LABELS, R.font.roboto)
            .setFont(CustomizationFont.RETRY_SCREEN_RETRY_BUTTON, R.font.roboto)

            .setFontSize(CustomizationFont.RETRY_SCREEN_TITLE_LABEL, 25)


            //Configuration success screen
            .setImage(CustomizationImage.SUCCESS_SCREEN_IMAGE, R.drawable.done_button)
            .setColor(CustomizationColor.SUCCESS_SCREEN_BACKGROUND, R.color.light_gray)
            .build()

        FaceSDK.Instance().customization.setUiConfiguration(uiConfiguration)

        FaceSDK.Instance().startLiveness(
            context
        ) {
            FaceSDK.Instance().customization.setUiConfiguration(UiConfiguration.Builder().build())
        }
    }

    override val title: String
        get() = "Liveness Screen Configuration"

    override val description: String
        get() = "Customization interface"
}