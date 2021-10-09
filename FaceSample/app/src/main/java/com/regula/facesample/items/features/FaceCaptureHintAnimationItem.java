package com.regula.facesample.items.features;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class FaceCaptureHintAnimationItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceCaptureConfiguration configuration = new FaceCaptureConfiguration.Builder()
                .setShowHelpTextAnimation(false)
                .build();
        FaceSDK.Instance().presentFaceCaptureActivity(context, configuration, faceCaptureResponse -> { });
    }

    @Override
    public String getTitle() {
        return "Disable FaceCapture NotificationTextView animation";
    }

    @Override
    public String getDescription() {
        return "Disables blinking NotificationTextView animation for FaceCapture.";
    }
}
