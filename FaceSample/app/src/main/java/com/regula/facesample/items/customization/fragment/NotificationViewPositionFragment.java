package com.regula.facesample.items.customization.fragment;

import android.graphics.RectF;
import android.view.View;

import androidx.constraintlayout.widget.Guideline;

import com.regula.facesample.R;
import com.regula.facesdk.fragment.FaceDefaultUiFragment;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class NotificationViewPositionFragment extends FaceDefaultUiFragment {

    @Override
    public void onFaceAreaViewPositionUpdated(RectF rect) {
        View view = getView();
        if (view == null)
            return;

        int topMargin = (int) rect.top;
        topMargin += rect.height() * 0.4;

        Guideline notificationGuideline = view.findViewById(R.id.topNotificationGuideline);
        notificationGuideline.setGuidelinePercent(-1);
        notificationGuideline.setGuidelineBegin(topMargin);
    }
}
