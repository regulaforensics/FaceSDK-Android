package com.regula.facesample.util;

import android.content.Context;
import android.widget.Toast;

import com.regula.facesdk.model.results.LivenessResponse;

public class LivenessResponseUtil {

   public static void response(Context context, LivenessResponse livenessResponse) {
      if (livenessResponse.getException() != null)
         Toast.makeText(context, "Error: " + livenessResponse.getException().getMessage(), Toast.LENGTH_SHORT).show();
      else
         Toast.makeText(context, "Liveness status: " + livenessResponse.getLiveness().name(), Toast.LENGTH_SHORT).show();
   }
}
