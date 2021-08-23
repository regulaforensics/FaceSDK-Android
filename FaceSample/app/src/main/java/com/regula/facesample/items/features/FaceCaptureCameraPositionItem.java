package com.regula.facesample.items.features;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class FaceCaptureCameraPositionItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceCaptureConfiguration configuration = new FaceCaptureConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .build();
        FaceSDK.Instance().presentFaceCaptureActivity(context, configuration, faceCaptureResponse -> { });
    }

    @Override
    public String getTitle() {
        return "Face Capture camera position";
    }

    @Override
    public String getDescription() {
        return "Changes default camera position to back.";
    }
}
