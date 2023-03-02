package com.regula.facesample.items.features;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;

public class FaceSdkDeinitItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceSDK.Instance().deinit();
    }

    @Override
    public String getDescription() {
        return "Deinitialization FaceSDK resources";
    }

    @Override
    public String getTitle() {
        return "Deinitialization SDK";
    }
}
