package com.regula.facesample.items.other;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class NetworkInterceptorItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceSDK.Instance().setNetworkInterceptorListener(httpURLConnection -> {
            httpURLConnection.getConnection().setRequestProperty("customFiels", "TestValue");
        });
        FaceSDK.Instance().setServiceUrl(FaceSDK.Instance().getServiceUrl());
        FaceSDK.Instance().startLiveness(context, livenessResponse -> {});
    }

    @Override
    public String getTitle() {
        return "Network interceptor";
    }

    @Override
    public String getDescription() {
        return "Adds custom http fields to the outgoing requests.";
    }
}
