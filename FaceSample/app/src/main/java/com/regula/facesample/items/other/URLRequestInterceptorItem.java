package com.regula.facesample.items.other;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class URLRequestInterceptorItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .setHeader("customFiels", "TestValue")
                .build();
        FaceSDK.Instance().setServiceUrl(FaceSDK.Instance().getServiceUrl());
        FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {});
    }

    @Override
    public String getTitle() {
        return "Custom service URL";
    }

    @Override
    public String getDescription() {
        return "Set up custom urls and add custom http fields to the outgoing requests.";
    }
}
