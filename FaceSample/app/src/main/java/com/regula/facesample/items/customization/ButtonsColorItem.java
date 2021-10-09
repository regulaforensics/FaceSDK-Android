package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.customization.fragment.ButtonsColorFragment;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class ButtonsColorItem extends CategoryItem {
    @Override
    public void onItemSelected(Context context) {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .setCameraSwitchEnabled(true)
                .registerUiFragmentClass(ButtonsColorFragment.class)
                .build();
        FaceSDK.Instance().startLiveness(context, configuration, faceCaptureResponse -> { });
    }

    @Override
    public String getDescription() {
        return "Change buttons color using Default UI";
    }

    @Override
    public String getTitle() {
        return "Change buttons color";
    }
}
