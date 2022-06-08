package com.regula.facesample.data;

import com.regula.facesample.items.ICategoryItem;
import com.regula.facesample.items.basic.FaceCaptureDefaultItem;
import com.regula.facesample.items.basic.LivenessDefaultItem;
import com.regula.facesample.items.basic.MatchFacesRequestItem;
import com.regula.facesample.items.customization.BasicCustomItem;
import com.regula.facesample.items.customization.AdvancedCustomItem;
import com.regula.facesample.items.customization.ButtonsColorItem;
import com.regula.facesample.items.customization.FlashButtonItem;
import com.regula.facesample.items.customization.HideCloseButtonItem;
import com.regula.facesample.items.customization.HideFlashButtonItem;
import com.regula.facesample.items.customization.HideNotificationViewItem;
import com.regula.facesample.items.customization.LivenessProcessingCustomItem;
import com.regula.facesample.items.customization.NotificationViewItem;
import com.regula.facesample.items.customization.NotificationViewPositionItem;
import com.regula.facesample.items.customization.OverlayCustomItem;
import com.regula.facesample.items.customization.SwapButtonItem;
import com.regula.facesample.items.features.FaceCaptureCameraPositionItem;
import com.regula.facesample.items.features.FaceCaptureHintAnimationItem;
import com.regula.facesample.items.features.LivenessAttemptsCountItem;
import com.regula.facesample.items.features.LivenessCameraSwitchItem;
import com.regula.facesample.items.features.LivenessHintAnimationItem;
import com.regula.facesample.items.other.LocalizationItem;
import com.regula.facesample.items.other.NetworkInterceptorItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class CategoryDataProvider {

    public List<ICategoryItem> getCategoryItems() {
        return new ArrayList<>(Arrays.asList(
                new HeaderItem() {
                    @Override
                    public String getTitle() {
                        return "Basic";
                    }
                },
                new LivenessDefaultItem(),
                new FaceCaptureDefaultItem(),
                new MatchFacesRequestItem(),
                new HeaderItem() {
                    @Override
                    public String getTitle() {
                        return "Feature Customization";
                    }
                },
                new LivenessCameraSwitchItem(),
                new LivenessAttemptsCountItem(),
                new LivenessHintAnimationItem(),
                new FaceCaptureCameraPositionItem(),
                new FaceCaptureHintAnimationItem(),
                new HeaderItem() {
                    @Override
                    public String getTitle() {
                        return "UI Customization";
                    }
                },
                new HideCloseButtonItem(),
                new HideFlashButtonItem(),
                new HideNotificationViewItem(),
                new NotificationViewItem(),
                new ButtonsColorItem(),
                new NotificationViewPositionItem(),
                new FlashButtonItem(),
                new SwapButtonItem(),
                new BasicCustomItem(),
                new AdvancedCustomItem(),
                new OverlayCustomItem(),
                new LivenessProcessingCustomItem(),
                new HeaderItem() {
                    @Override
                    public String getTitle() {
                        return "Other";
                    }
                },
                new LocalizationItem(),
                new NetworkInterceptorItem()
        ));
    }

    abstract static class HeaderItem implements ICategoryItem {

        @Override
        public boolean isHeader() {
            return true;
        }
    }
}
