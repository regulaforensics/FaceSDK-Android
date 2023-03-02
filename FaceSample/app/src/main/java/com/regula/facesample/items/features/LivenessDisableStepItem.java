package com.regula.facesample.items.features;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;
import com.regula.facesdk.enums.LivenessSkipStep;

public class LivenessDisableStepItem extends CategoryItem {

   @Override
   public void onItemSelected(Context context) {
      LivenessConfiguration configuration = new LivenessConfiguration.Builder()
              .setSkipStep(LivenessSkipStep.START_STEP, LivenessSkipStep.DONE_STEP)
              .build();
      FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {});
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
