package com.regula.facesample.items.features;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LivenessAttemptsCountItem extends CategoryItem {
    @Override
    public void onItemSelected(Context context) {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .setAttemptsCount(2)
                .build();
        FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {});
    }

    @Override
    public String getTitle() {
        return "Limit Liveness attempts count";
    }

    @Override
    public String getDescription() {
        return "Liveness is limited to the number of retries.";
    }
}
