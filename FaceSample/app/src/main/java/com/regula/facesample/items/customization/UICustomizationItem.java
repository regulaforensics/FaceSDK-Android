package com.regula.facesample.items.customization;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.animation.AccelerateInterpolator;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class UICustomizationItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        ValueAnimator mTimerAnimator = ValueAnimator.ofFloat(0.9f, 0.1f);

        JSONObject jsonObject = getJsonFromAssets(context, "layer.json");

        LivenessConfiguration configuration = new LivenessConfiguration.Builder()
                .setCloseButtonEnabled(false)
                .build();

        FaceSDK.Instance().getCustomization().setUiCustomizationLayer(jsonObject);

        FaceSDK.Instance().setOnClickListener(view -> {
            if ((int) view.getTag() == 101) {
                FaceSDK.Instance().stopLivenessProcessing(context);
                mTimerAnimator.cancel();
            }
        });

        FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {
            FaceSDK.Instance().getCustomization().setUiCustomizationLayer(null);
            if (mTimerAnimator != null)
                mTimerAnimator.cancel();
        });

        initAnimation(mTimerAnimator, jsonObject);
    }

    private JSONObject getJsonFromAssets(Context context, String name) {
        try {
            InputStream inputStream = context.getAssets().open(name);
            String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
            return new JSONObject(jsonString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initAnimation(ValueAnimator mTimerAnimator, JSONObject jsonObject) {
        mTimerAnimator.setDuration(1000);
        mTimerAnimator.setInterpolator(new AccelerateInterpolator());
        mTimerAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mTimerAnimator.setRepeatCount(20);
        mTimerAnimator.addUpdateListener(valueAnimator -> {
            updatePosition(jsonObject, (float) valueAnimator.getAnimatedValue());
            FaceSDK.Instance().getCustomization().setUiCustomizationLayer(jsonObject);
        });
        mTimerAnimator.start();
    }

    private void updatePosition(JSONObject jsonObject, Float position) {
        if (jsonObject != null) {
            try {
                ((JSONObject) jsonObject.getJSONArray("objects").get(3)).getJSONObject("image")
                        .put("alpha", position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getDescription() {
        return "Custom labels, images and buttons to the camera screen";
    }

    @Override
    public String getTitle() {
        return "Custom UI layer";
    }
}
