package com.regula.facesample.items.customization.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.regula.facesample.R;
import com.regula.facesdk.fragment.FaceDefaultUiFragment;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class NotificationViewFragment extends FaceDefaultUiFragment {

    @Override
    public Drawable getNotificationBackgroundDrawable(Context context, boolean isLightOn) {
        return ContextCompat.getDrawable(context, isLightOn ? R.drawable.notification_view_background_dark : R.drawable.notification_view_background_white);
    }

    @Override
    public int getNotificationTextColor(boolean isLightOn) {
        return getResources().getColor(isLightOn ? R.color.notification_view_text_color_light_on : R.color.notification_view_text_color_light_off);
    }
}
