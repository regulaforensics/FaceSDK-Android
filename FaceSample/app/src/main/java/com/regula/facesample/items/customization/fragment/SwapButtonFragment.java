package com.regula.facesample.items.customization.fragment;

import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.regula.facesample.R;
import com.regula.facesdk.fragment.FaceDefaultUiFragment;

/**
 * Created by Sergey Yakimchik on 7.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class SwapButtonFragment extends FaceDefaultUiFragment {

    @Override
    protected View getSwapCameraButton(@NonNull View v) {
        View swapCameraBtn = super.getSwapCameraButton(v);
        if (swapCameraBtn instanceof ImageButton) {
            ((ImageButton) swapCameraBtn).setImageResource(R.drawable.swap_button);
        }
        return swapCameraBtn;
    }
}
