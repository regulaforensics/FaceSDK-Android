package com.regula.facesample.items.features;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesample.util.LivenessResponseUtil;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;
import com.regula.facesdk.enums.LivenessSkipStep;

public class LivenessDisableStepItem extends CategoryItem {

   @Override
   public void onItemSelected(Context context) {
      LivenessConfiguration configuration = new LivenessConfiguration.Builder()
              .setSkipStep(LivenessSkipStep.ONBOARDING_STEP, LivenessSkipStep.SUCCESS_STEP)
              .build();
      FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> LivenessResponseUtil.response(context, livenessResponse));
   }

   @Override
   public String getDescription() {
      return "Hide some steps in liveness process";
   }

   @Override
   public String getTitle() {
      return "Disable steps in liveness";
   }
}
