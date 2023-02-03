package com.regula.facesample.items.other;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.regula.facesample.items.CategoryItem;
import com.regula.facesdk.FaceSDK;
import com.regula.facesdk.listener.IVideoEncoderCompletion;

public class VideoCompletionItem extends CategoryItem {

    @Override
    public void onItemSelected(Context context) {
        FaceSDK.Instance().setVideoEncoderCompletion(new IVideoEncoderCompletion() {
            @Override
            public void onVideoRecorded(@NonNull String transactionId, boolean success) {
                Log.d("VideoCompletionItem", "Transaction video: " + transactionId + " success: " + success);
            }
        });
        FaceSDK.Instance().startLiveness(context, livenessResponse -> { });
    }

    @Override
    public String getDescription() {
        return "Video completion about uploading liveness video to service";
    }

    @Override
    public String getTitle() {
        return "Video completion";
    }
}
