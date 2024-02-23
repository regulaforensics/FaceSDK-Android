package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.util.FaceCaptureResponseUtil;
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
                .setCloseButtonEnabled(false)
                .build();
        FaceSDK.Instance().presentFaceCaptureActivity(context, configuration, faceCaptureResponse -> FaceCaptureResponseUtil.response(context, faceCaptureResponse));
    }

    @Override
    public String getDescription() {
        return "Hide close button using default UI";
    }

    @Override
    public String getTitle() {
        return "Hide close button";
    }
}
