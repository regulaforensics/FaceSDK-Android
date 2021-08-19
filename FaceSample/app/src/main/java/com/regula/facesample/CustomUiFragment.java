package com.regula.facesample;

import com.regula.facesdk.fragment.FaceUiFragment;

/**
 * Created by Sergey Yakimchik on 19.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class CustomUiFragment extends FaceUiFragment {

    @Override
    protected int getSwapCameraButtonId() {
        return 0;
    }

    @Override
    protected int getLightButtonId() {
        return 0;
    }

    @Override
    protected int getNotificationTextViewId() {
        return R.id.notificationView;
    }

    @Override
    protected int getLightButtonOnDrawableId() {
        return 0;
    }

    @Override
    protected int getLightButtonOffDrawableId() {
        return 0;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.custom_ui;
    }

    @Override
    protected int getCloseButtonId() {
        return R.id.closeButton;
    }
}
