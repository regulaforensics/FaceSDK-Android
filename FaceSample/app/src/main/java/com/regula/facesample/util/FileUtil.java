package com.regula.facesample.util;

import android.content.Context;

import com.regula.facesample.R;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

   public static byte[] getLicense(Context context) {
      if (context == null)
         return null;

      InputStream licInput = context.getResources().openRawResource(R.raw.regula);
      int available;
      try {
         available = licInput.available();
      } catch (IOException e) {
         return null;
      }
      byte[] license = new byte[available];
      try {
         licInput.read(license);
      } catch (IOException e) {
         return null;
      }

      return license;
   }
}
