package com.regula.facesample.items.basic;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class FaceCaptureDefaultItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceSDK.Instance().presentFaceCaptureActivity(context, faceCaptureResponse -> { });
    }

    @Override
    public String getTitle() {
        return "FaceCapture";
    }

    @Override
    public String getDescription() {
        return "Automaticlly captures face photo.";
    }
}
