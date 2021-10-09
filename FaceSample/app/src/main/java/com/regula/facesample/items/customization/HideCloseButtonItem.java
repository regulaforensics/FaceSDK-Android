package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.customization.fragment.HideCloseButtonFragment;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class HideCloseButtonItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceCaptureConfiguration configuration = new FaceCaptureConfiguration.Builder()
                .registerUiFragmentClass(HideCloseButtonFragment.class)
                .build();
        FaceSDK.Instance().presentFaceCaptureActivity(context, configuration, faceCaptureResponse -> { });
    }

    @Override
    public String getDescription() {
        return "Hide close button using default UI fragment";
    }

    @Override
    public String getTitle() {
        return "Hide close button";
    }
}
