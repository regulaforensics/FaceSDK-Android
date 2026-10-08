package com.regula.facesample.items.basic;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.util.LivenessResponseUtil;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;
import com.regula.facesdk.enums.LivenessType;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LivenessPassiveWithBlinkItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .setType(LivenessType.PASSIVE_WITH_BLINK)
                .build();
        FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> LivenessResponseUtil.response(context, livenessResponse));
    }

    @Override
    public String getTitle() {
        return "Liveness – Passive with Blink";
    }

    @Override
    public String getDescription() {
        return "Detects if a person on camera is alive.";
    }
}