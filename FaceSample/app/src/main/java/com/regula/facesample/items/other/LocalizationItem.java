package com.regula.facesample.items.other;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;

/**
 * Created by Sergey Yakimchik on 24.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LocalizationItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        // uncomment in res/values/strings.xml:
        // hint.fit resource
        FaceSDK.Instance().startLiveness(context, livenessResponse -> { });
    }

    @Override
    public String getTitle() {
        return "Custom localization";
    }

    @Override
    public String getDescription() {
        return "Localization hook example.";
    }
}
