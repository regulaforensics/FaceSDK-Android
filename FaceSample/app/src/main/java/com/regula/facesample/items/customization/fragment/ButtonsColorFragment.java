package com.regula.facesample.items.customization.fragment;

import android.graphics.Color;

import com.regula.facesdk.fragment.FaceDefaultUiFragment;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class ButtonsColorFragment extends FaceDefaultUiFragment {

    @Override
    public int getButtonsColor(boolean isLightOn) {
        return isLightOn ? Color.RED : Color.GREEN;
    }
}
