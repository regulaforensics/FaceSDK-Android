package com.regula.facesample.items.customization.fragment;

import com.regula.facesample.R;
import com.regula.facesdk.fragment.FaceUiFragment;

/**
 * Created by Sergey Yakimchik on 19.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LivenessHintViewAppearanceFragment extends FaceUiFragment {

    @Override
    protected int getSwapCameraButtonId() {
        return 0;
    }

    @Override
    protected int getFlashLightButtonId() {
        return 0;
    }

    @Override
    protected int getNotificationTextViewId() {
        return R.id.notificationView;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.custom_hint_view_appearance;
    }

    @Override
    protected int getCloseButtonId() {
        return R.id.closeButton;
    }
}
