package com.regula.facesample.items.customization;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.items.customization.fragment.LivenessProcessingCustomFragment;
import com.regula.facesample.util.LivenessResponseUtil;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

/**
 * Created by Sergey Yakimchik on 29.04.22.
 * Copyright (c) 2022 Regula. All rights reserved.
 */

public class LivenessProcessingCustomItem extends CategoryItem {

   @Override
   public void onItemSelected(Context context) {
      LivenessConfiguration configuration = new LivenessConfiguration.Builder()
              .registerProcessingFragment(LivenessProcessingCustomFragment.class)
              .build();
      FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> LivenessResponseUtil.response(context, livenessResponse));
   }

   @Override
   public String getDescription() {
      return "Customize liveness processing and retry screens";
   }

   @Override
   public String getTitle() {
      return "Custom liveness process";
   }
}
