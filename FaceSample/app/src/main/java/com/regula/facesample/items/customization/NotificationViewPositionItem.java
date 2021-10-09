package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.customization.fragment.NotificationViewPositionFragment;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class NotificationViewPositionItem extends CategoryItem {
    @Override
    public void onItemSelected(Context context) {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .registerUiFragmentClass(NotificationViewPositionFragment.class)
                .build();
        FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {});
    }

    @Override
    public String getDescription() {
        return "Change notification view position using default UI";
    }

    @Override
    public String getTitle() {
        return "Notification text view position";
    }
}
