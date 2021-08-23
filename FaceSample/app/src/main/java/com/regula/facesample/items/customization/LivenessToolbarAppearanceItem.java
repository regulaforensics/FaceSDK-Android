package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.customization.fragment.LivenessToolbarAppearanceFragment;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LivenessToolbarAppearanceItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .setUiFragmentClass(LivenessToolbarAppearanceFragment.class)
                .build();
        FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {});
    }

    @Override
    public String getTitle() {
        return "Liveness toolbar appearance";
    }

    @Override
    public String getDescription() {
        return "Usage example for toolbar.";
    }
}
