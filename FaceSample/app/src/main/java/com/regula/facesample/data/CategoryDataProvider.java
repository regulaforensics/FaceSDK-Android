package com.regula.facesample.data;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.basic.FaceCaptureDefaultItem;
import com.regula.facesample.items.basic.LivenessDefaultItem;
import com.regula.facesample.items.basic.MatchFacesRequestItem;
import com.regula.facesample.items.customization.LivenessHintViewAppearanceItem;
import com.regula.facesample.items.customization.LivenessToolbarAppearanceItem;
import com.regula.facesample.items.features.FaceCaptureCameraPositionItem;
import com.regula.facesample.items.features.FaceCaptureHintAnimationItem;
import com.regula.facesample.items.features.LivenessAttemptsCountItem;
import com.regula.facesample.items.features.LivenessCameraSwitchItem;
import com.regula.facesample.items.features.LivenessHintAnimationItem;
import com.regula.facesample.items.other.URLRequestInterceptorItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class CategoryDataProvider {

    public List<CategoryItem> getCategoryItems() {
        return new ArrayList<>(Arrays.asList(
                new LivenessDefaultItem(),
                new FaceCaptureDefaultItem(),
                new MatchFacesRequestItem(),
                new LivenessCameraSwitchItem(),
                new LivenessAttemptsCountItem(),
                new LivenessHintAnimationItem(),
                new FaceCaptureCameraPositionItem(),
                new FaceCaptureHintAnimationItem(),
                new LivenessHintViewAppearanceItem(),
                new LivenessToolbarAppearanceItem(),
                new URLRequestInterceptorItem()
        ));
    }
}
