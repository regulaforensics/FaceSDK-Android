package com.regula.facesample.items.customization.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.regula.facesdk.fragment.FaceDefaultUiFragment;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class HideNotificationViewFragment extends FaceDefaultUiFragment {

    @Override
    protected TextView getNotificationView(@NonNull View view) {
        TextView textView = super.getNotificationView(view);
        textView.setVisibility(View.INVISIBLE);
        return textView;
    }

    @Override
    public void setNotificationStatusText(String s) {
        // ignore any new status
    }
}
