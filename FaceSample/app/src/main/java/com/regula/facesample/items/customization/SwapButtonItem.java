package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.customization.fragment.SwapButtonFragment;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 7.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class SwapButtonItem extends CategoryItem {
    @Override
    public void onItemSelected(Context context) {
        FaceCaptureConfiguration configuration = new FaceCaptureConfiguration.Builder()
                .registerUiFragmentClass(SwapButtonFragment.class)
                .setCameraSwitchEnabled(true)
                .build();
        FaceSDK.Instance().presentFaceCaptureActivity(context, configuration, livenessResponse -> {});
    }

    @Override
    public String getDescription() {
        return "Change swap button drawable using default UI";
    }

    @Override
    public String getTitle() {
        return "Change swap button";
    }
}
