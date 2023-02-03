package com.regula.facesample.items.features;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.configuration.LivenessConfiguration;

import java.util.UUID;

public class LivenessSessionIdItem extends CategoryItem {

   @Override
   public void onItemSelected(Context context) {
      LivenessConfiguration configuration = new LivenessConfiguration.Builder()
              .setSessionId(UUID.randomUUID().toString())
              .build();
      FaceSDK.Instance().startLiveness(context, configuration, livenessResponse -> {});
   }

   @Override
   public String getDescription() {
      return "Set up session Id for liveness";
   }

   @Override
   public String getTitle() {
      return "Liveness Session Id";
   }
}
