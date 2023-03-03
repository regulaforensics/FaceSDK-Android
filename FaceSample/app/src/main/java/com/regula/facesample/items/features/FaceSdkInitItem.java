package com.regula.facesample.items.features;

import android.content.Context;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;

public class FaceSdkInitItem extends CategoryItem {

   @Override
   public void onItemSelected(Context context) {
      FaceSDK.Instance().init(context, (status, exception) -> { });
   }

   @Override
   public String getDescription() {
      return "Initialization FaceSDK resources";
   }

   @Override
   public String getTitle() {
      return "Initialization SDK";
   }
}
