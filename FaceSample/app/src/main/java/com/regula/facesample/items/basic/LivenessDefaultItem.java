package com.regula.facesample.items.basic;

import android.content.Context;
import android.widget.Toast;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.util.LivenessResponseUtil;
import com.regula.facesdk.FaceSDK;

/**
 * Created by Sergey Yakimchik on 20.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LivenessDefaultItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceSDK.Instance().startLiveness(context, livenessResponse -> LivenessResponseUtil.response(context, livenessResponse));
    }

    @Override
    public String getTitle() {
        return "Liveness";
    }

    @Override
    public String getDescription() {
        return "Detects if a person on camera is alive.";
    }
}