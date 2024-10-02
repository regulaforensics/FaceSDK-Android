package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.customization.fragment.HideNotificationViewFragment;
import com.regula.facesample.util.FaceCaptureResponseUtil;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.FaceCaptureConfiguration;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class HideNotificationViewItem extends CategoryItem {
    @Override
    public void onItemSelected(Context context) {
        FaceCaptureConfiguration configuration = new FaceCaptureConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .registerUiFragmentClass(HideNotificationViewFragment.class)
                .build();
        FaceSDK.Instance().presentFaceCaptureActivity(context, configuration, faceCaptureResponse -> FaceCaptureResponseUtil.response(context, faceCaptureResponse));
    }

    @Override
    public String getDescription() {
        return "Hide notification text view using default UI fragment";
    }

    @Override
    public String getTitle() {
        return "Hide notification text view";
    }
}
