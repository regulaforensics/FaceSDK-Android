package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;

/**
 * Created by Sergey Yakimchik on 24.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class OverlayCustomItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        // uncomment in res/values/colors.xml:
        // all
        // reg_face_overlay_background_dark
        // reg_face_overlay_background_white
        // reg_face_overlay_border_default
        // reg_face_overlay_border_active
        FaceSDK.Instance().startLiveness(context, livenessResponse -> { });
    }

    @Override
    public String getTitle() {
        return "Custom Overlay";
    }

    @Override
    public String getDescription() {
        return "Change colors for overlay";
    }
}
