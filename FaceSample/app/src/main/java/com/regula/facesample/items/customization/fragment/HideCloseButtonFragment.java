package com.regula.facesample.items.customization.fragment;

import android.view.View;

import com.regula.facesdk.fragment.FaceDefaultUiFragment;

/**
 * Created by Sergey Yakimchik on 6.09.21.
 * Copyright (c) 2021 Regula. All rights reserved.
 */

public class HideCloseButtonFragment extends FaceDefaultUiFragment {

    @Override
    public View getCloseButton(View view) {
        View button = super.getCloseButton(view);
        button.setVisibility(View.INVISIBLE);
        return button;
    }
}
