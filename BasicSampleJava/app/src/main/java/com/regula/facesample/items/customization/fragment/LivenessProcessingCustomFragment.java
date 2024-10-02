package com.regula.facesample.items.customization.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.regula.facesample.R;
import com.regula.facesdk.fragment.BaseFaceLivenessProcessingFragment;

/**
 * Created by Sergey Yakimchik on 04/29/22.
 * Copyright (c) 2020 Regula. All rights reserved.
 */

public final class LivenessProcessingCustomFragment extends BaseFaceLivenessProcessingFragment {

    private TextView guidelinesTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null)
            return null;

        guidelinesTextView = view.findViewById(R.id.guidelines_text_view);

        return view;
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.custom_liveness_processing;
    }

    @Override
    public View getRetryButton(View view) {
        return view.findViewById(R.id.retry_button);
    }

    @Override
    public View getCancelButton(View view) {
        return view.findViewById(R.id.cancel_button);
    }

    @Override
    public void faceSdkError(int[] stringResourceId) {
        StringBuilder guidelineTextBuilder = new StringBuilder();
        if (stringResourceId == null) {
            stringResourceId = new int[] { com.regula.facesdk.R.string.livenessRetry_text_environment, com.regula.facesdk.R.string.livenessRetry_text_subject };
            Log.e("CustomFragment", "Not defined error caught");
        }

        for (int strId : stringResourceId) {
            guidelineTextBuilder.append(String.format("- %s\n", getString(strId)));
        }

        guidelinesTextView.setText(guidelineTextBuilder);
    }

    @Override
    public View getProcessingLayout(View v) {
        return v.findViewById(R.id.processing_layout);
    }

    @Override
    public View getRetryLayout(View v) {
        return v.findViewById(R.id.result_layout);
    }

    @Override
    public View getVerifyLayout(View view) {
        return view.findViewById(R.id.verifiedLayout);
    }

    @Override
    public View getCloseButton(View v) {
        return v.findViewById(R.id.close_button);
    }

}
