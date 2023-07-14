package com.regula.facepersonsearch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import androidx.core.app.ActivityCompat;

import com.regula.facesdk.request.personDb.ImageUpload;

public class Common {
    public static final int PICK_IMAGE = 123;
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 321;

    public static  void pickImage(Activity activity){
        if(ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        } else {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
            );
        }
    }

    static class ImageUploadWithThumbnail extends ImageUpload{
        private Bitmap thumbnail;

        public Bitmap getThumbnail(){
            if(thumbnail == null) {
                thumbnail = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeByteArray(getImageData(), 0, getImageData().length),
                        500, 500);
            }
            return thumbnail;
        }
    }
}
