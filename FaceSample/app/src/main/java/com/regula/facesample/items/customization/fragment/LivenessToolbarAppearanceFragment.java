package com.regula.facesample.items.customization.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;

import com.regula.facesample.R;
import com.regula.facesdk.fragment.FaceUiFragment;

/**
 * Created by Sergey Yakimchik on 23.08.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class LivenessToolbarAppearanceFragment extends FaceUiFragment {

    @Override
    protected int getSwapCameraButtonId() {
        return R.id.swapCameraBtn;
    }

    @Override
    protected int getFlashLightButtonId() {
        return R.id.lightBtn;
    }

    @Override
    protected int getNotificationTextViewId() {
        return R.id.notificationTv;
    }

    @Override
    protected int getResourceLayoutId() {
        return R.layout.custom_toolbar_appearance;
    }

    @Override
    protected int getCloseButtonId() {
        return R.id.exitBtn;
    }

    @Override
    public void onLightButtonClicked(boolean isLightOn) {
        super.onLightButtonClicked(isLightOn);
        mLightBtn.setImageDrawable(getResources().getDrawable(isLightOn ? R.drawable.flash_light_on : R.drawable.flash_light_off));
    }

    @Override
    public void onScreenLightChanged(boolean isScreenLightOn) {
        int buttonsColor = getButtonsColor(isScreenLightOn);

        mCloseBtn.setColorFilter(buttonsColor, PorterDuff.Mode.SRC_ATOP);
        mLightBtn.setColorFilter(buttonsColor, PorterDuff.Mode.SRC_ATOP);
        mCameraSwapBtn.setColorFilter(buttonsColor, PorterDuff.Mode.SRC_ATOP);
    }

    protected int getButtonsColor(boolean isLightOn) {
        return isLightOn ? Color.BLACK : Color.WHITE;
    }
}
