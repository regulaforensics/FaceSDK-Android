package com.regula.facesample.util;

import android.content.Context;
import android.widget.Toast;

import com.regula.facesdk.model.results.FaceCaptureResponse;
import com.regula.facesdk.model.results.LivenessResponse;

public class FaceCaptureResponseUtil {

   public static void response(Context context, FaceCaptureResponse faceCaptureResponse) {
      if (faceCaptureResponse.getException() != null)
         Toast.makeText(context, "Error: " + faceCaptureResponse.getException().getMessage(), Toast.LENGTH_SHORT).show();
      else
         Toast.makeText(context, "Successfully got an image", Toast.LENGTH_SHORT).show();
   }
}
