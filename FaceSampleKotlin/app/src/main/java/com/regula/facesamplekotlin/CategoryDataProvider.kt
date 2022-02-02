package com.regula.facesamplekotlin

import com.regula.facesamplekotlin.category.HeaderItem
import com.regula.facesamplekotlin.category.ICategoryItem
import com.regula.facesamplekotlin.fragments.*
import com.regula.facesamplekotlin.items.FaceCaptureDefaultItem
import com.regula.facesamplekotlin.items.LivenessDefaultItem
import com.regula.facesamplekotlin.items.MatchFacesRequestItem
import com.regula.facesamplekotlin.items.NetworkInterceptorItem
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.configuration.LivenessConfiguration

class CategoryDataProvider {
    companion object {
        fun getCategoryData(): List<ICategoryItem> {
            return listOf(
                HeaderItem(R.string.basic),
                LivenessDefaultItem(R.string.liveness, R.string.liveness_description),
                FaceCaptureDefaultItem(R.string.face_capture, R.string.face_capture_description),
                MatchFacesRequestItem(),
                HeaderItem(R.string.feature_customization),
                LivenessDefaultItem(
                    R.string.ln_camera_switch,
                    R.string.ln_camera_switch_description,
                    switchItemConfigLn()
                ),
                LivenessDefaultItem(
                    R.string.ln_attempts_count,
                    R.string.ln_attempts_count_description,
                    attemptsCountConfigLn()
                ),
                LivenessDefaultItem(
                    R.string.ln_disable_notification,
                    R.string.ln_disable_notification_description,
                    hideHelpTextAnimationConfigLn()
                ),
                FaceCaptureDefaultItem(
                    R.string.fc_camera_position,
                    R.string.fc_camera_position_description,
                    switchCameraConfigFc()
                ),
                FaceCaptureDefaultItem(
                    R.string.fc_disable_notification,
                    R.string.fc_disable_notification_description,
                    showHelpTextAnimationConfigFc()
                ),
                HeaderItem(R.string.ui_customization),

                FaceCaptureDefaultItem(
                    R.string.hide_close_button,
                    R.string.hide_close_button_description,
                    hideCloseButtonConfigFc()
                ),
                FaceCaptureDefaultItem(
                    R.string.hide_notification_text,
                    R.string.hide_notification_text_description,
                    hideNotificationViewConfigFc()
                ),
                LivenessDefaultItem(
                    R.string.custom_notification_background,
                    R.string.custom_notification_background_description,
                    notificationViewItemConfigLn()
                ),
                LivenessDefaultItem(
                    R.string.change_buttons_color,
                    R.string.change_buttons_color_description,
                    buttonColorConfigLn()
                ),
                LivenessDefaultItem(
                    R.string.notification_text_position,
                    R.string.notification_text_position_description,
                    notificationViewConfigLn()
                ),
                LivenessDefaultItem(
                    R.string.custom_flash_button,
                    R.string.custom_flash_button_description,
                    flashButtonConfigLn()
                ),
                LivenessDefaultItem(
                    R.string.change_swap_button,
                    R.string.change_swap_button_description,
                    swapButtonConfigLn()
                ),
                LivenessDefaultItem(
                    R.string.custom_ui_fragment,
                    R.string.custom_ui_fragment_description,
                    basicCustomConfigLn()
                ),
                LivenessDefaultItem(
                    R.string.ln_toolbar_appearance,
                    R.string.ln_toolbar_appearance_description,
                    advancedCustomConfigLn()
                ),
                FaceCaptureDefaultItem(
                    // uncomment in res/values/colors.xml:
                    // all
                    // facesdk_overlay_background_dark
                    // facesdk_overlay_background_white
                    // facesdk_overlay_border_default
                    // facesdk_overlay_border_active
                    R.string.custom_overlay,
                    R.string.custom_overlay_description
                ),
                HeaderItem(R.string.other),
                LivenessDefaultItem(
                    // uncomment in res/values/strings.xml:
                    // hint.fit resource
                    R.string.custom_localization,
                    R.string.custom_localization_description
                ),
                NetworkInterceptorItem()
            )
        }


        private fun switchItemConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .build()
        }

        private fun attemptsCountConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .setAttemptsCount(2)
                .build()
        }

        private fun hideHelpTextAnimationConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .setShowHelpTextAnimation(false)
                .build()
        }

        private fun notificationViewItemConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .registerUiFragmentClass(NotificationViewFragment::class.java)
                .setCameraSwitchEnabled(true)
                .build()
        }


        private fun buttonColorConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .registerUiFragmentClass(ButtonsColorFragment::class.java)
                .build()
        }

        private fun notificationViewConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .registerUiFragmentClass(NotificationViewPositionFragment::class.java)
                .build()
        }

        private fun flashButtonConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .registerUiFragmentClass(FlashButtonFragment::class.java)
                .setCameraSwitchEnabled(true)
                .build()
        }


        private fun basicCustomConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .registerUiFragmentClass(BasicCustomUiFragment::class.java)
                .build()
        }

        private fun advancedCustomConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .registerUiFragmentClass(AdvancedCustomUiFragment::class.java)
                .build()
        }

        private fun swapButtonConfigLn(): LivenessConfiguration {
            return LivenessConfiguration.Builder()
                .registerUiFragmentClass(SwapButtonFragment::class.java)
                .setCameraSwitchEnabled(true)
                .build()
        }

        private fun switchCameraConfigFc(): FaceCaptureConfiguration {
            return FaceCaptureConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .build()
        }

        private fun showHelpTextAnimationConfigFc(): FaceCaptureConfiguration {
            return FaceCaptureConfiguration.Builder()
                .setShowHelpTextAnimation(false)
                .build()
        }


        private fun hideCloseButtonConfigFc(): FaceCaptureConfiguration {
            return FaceCaptureConfiguration.Builder()
                .registerUiFragmentClass(HideCloseButtonFragment::class.java)
                .build()
        }

        private fun hideNotificationViewConfigFc(): FaceCaptureConfiguration {
            return FaceCaptureConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .registerUiFragmentClass(HideNotificationViewFragment::class.java)
                .build()
        }
    }
}