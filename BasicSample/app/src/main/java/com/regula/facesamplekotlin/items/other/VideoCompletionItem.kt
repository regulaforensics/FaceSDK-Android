package com.regula.facesamplekotlin.items.other

import android.content.Context
import android.util.Log
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK

class VideoCompletionItem: CategoryItem() {

    override fun onItemSelected(context: Context) {
        FaceSDK.Instance().setVideoEncoderCompletion { transactionId, success ->
            Log.d("VideoCompletionItem", "Transaction video: $transactionId success: $success")
        }
        FaceSDK.Instance().startLiveness(context) { }
    }

    override val title: String
        get() = "Video completion about uploading\nliveness video to service"

    override val description: String
        get() = "Video completion"
}