package com.regula.facepersonsearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.regula.facesdk.request.person.ImageUpload;

import java.io.ByteArrayOutputStream;

public class Common {

    public static byte[] transformBitmapToByte(Bitmap source) {
        Bitmap result = null;

        if (source != null) {
            int width = source.getWidth();
            int height = source.getHeight();

            int minSize = 400;
            if (Math.min(width, height) <= minSize) {

                float bitmapRatio = (float) width / height;

                if (bitmapRatio < 1) {
                    width = minSize;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = minSize;
                    width = (int) (height * bitmapRatio);
                }

                result = Bitmap.createScaledBitmap(source, width, height, true);

                source.recycle();
            } else {
                result = source;
            }
        }

        if (result != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            source.recycle();
            return stream.toByteArray();
        }

        return null;
    }

    static class ImageUploadWithThumbnail extends ImageUpload {
        private Bitmap thumbnail;

        public Bitmap getThumbnail() {
            if (thumbnail == null) {
                thumbnail = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeByteArray(getImageData(), 0, getImageData().length),
                        500, 500);
            }
            return thumbnail;
        }

        public void setThumbnail(Bitmap thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}
