package com.regula.facesamplekotlin.util

import android.content.Context
import android.widget.Toast
import com.regula.facesdk.model.results.FaceCaptureResponse

object FaceCaptureResponseUtil {
    fun response(context: Context?, faceCaptureResponse: FaceCaptureResponse) {
        if (faceCaptureResponse.exception != null)
            Toast.makeText(context, "Error: " + faceCaptureResponse.exception!!.message, Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Successfully got an image", Toast.LENGTH_SHORT).show()
    }
}