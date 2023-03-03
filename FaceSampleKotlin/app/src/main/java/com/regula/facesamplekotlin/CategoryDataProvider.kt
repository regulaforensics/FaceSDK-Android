package com.regula.facesamplekotlin

import com.regula.facesamplekotlin.category.HeaderItem
import com.regula.facesamplekotlin.category.ICategoryItem
import com.regula.facesamplekotlin.items.basic.DetectFacesRequestItem
import com.regula.facesamplekotlin.items.basic.FaceImageQualityRequestItem
import com.regula.facesamplekotlin.items.basic.FaceCaptureDefaultItem
import com.regula.facesamplekotlin.items.basic.LivenessDefaultItem
import com.regula.facesamplekotlin.items.basic.MatchFacesRequestItem
import com.regula.facesamplekotlin.items.customization.*
import com.regula.facesamplekotlin.items.features.*
import com.regula.facesamplekotlin.items.other.LocalizationItem
import com.regula.facesamplekotlin.items.other.NetworkInterceptorItem
import com.regula.facesamplekotlin.items.other.VideoCompletionItem

class CategoryDataProvider {
    companion object {
        fun getCategoryData(): List<ICategoryItem> {
            return listOf(
                HeaderItem("Basic"),
                LivenessDefaultItem(),
                FaceCaptureDefaultItem(),
                MatchFacesRequestItem(),
                DetectFacesRequestItem(),
                FaceImageQualityRequestItem(),
                HeaderItem( "Feature Customization"),
                LivenessAttemptsCountItem(),
                LivenessHintAnimationItem(),
                FaceCaptureCameraPositionItem(),

                FaceCaptureHintAnimationItem(),
                LivenessDisableStepItem(),
                LivenessSessionIdItem(),
                FaceSdkInitItem(),
                FaceSdkDeinitItem(),
                HeaderItem("UI Customization"),

                HideCloseButtonItem(),
                HideFlashButtonItem(),
                HideNotificationViewItem(),
                NotificationViewItem(),
                ButtonsColorItem(),
                NotificationViewPositionItem(),
                FlashButtonItem(),
                SwapButtonItem(),
                BasicCustomItem(),
                AdvancedCustomItem(),
                OverlayCustomItem(),
                LivenessProcessingCustomItem(),

                HeaderItem("Other"),

                LocalizationItem(),
                NetworkInterceptorItem(),
                VideoCompletionItem()
            )
        }
    }
}