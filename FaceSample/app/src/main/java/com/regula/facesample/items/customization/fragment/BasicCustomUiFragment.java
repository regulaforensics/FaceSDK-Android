package com.regula.facesample.items.customization.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.regula.facesample.R;
import com.regula.facesdk.fragment.FaceUiFragment;

/**
 * Created by Sergey Yakimchik on 19.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class BasicCustomUiFragment extends FaceUiFragment {

    @Override
    public View getSwapCameraButton(@NonNull View view) {
        return null;
    }

    @Override
    public View getFlashLightButton(@NonNull View view) {
        return null;
    }

    @Override
    public TextView getNotificationView(@NonNull View view) {
        return view.findViewById(R.id.notificationView);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.custom_hint_view_appearance;
    }

    @Override
    public View getCloseButton(View view) {
        return view.findViewById(R.id.closeButton);
    }
}
