package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.customization.fragment.FlashButtonFragment;
import com.regula.facesample.util.FaceCaptureResponseUtil;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 7.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class FlashButtonItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceCaptureConfiguration configuration = new FaceCaptureConfiguration.Builder()
                .registerUiFragmentClass(FlashButtonFragment.class)
                .setCameraSwitchEnabled(true)
                .build();
        FaceSDK.Instance().presentFaceCaptureActivity(context, configuration, faceCaptureResponse -> FaceCaptureResponseUtil.response(context, faceCaptureResponse));
    }

    @Override
    public String getDescription() {
        return "Change flash button using default UI";
    }

    @Override
    public String getTitle() {
        return "Custom flash button";
    }
}
