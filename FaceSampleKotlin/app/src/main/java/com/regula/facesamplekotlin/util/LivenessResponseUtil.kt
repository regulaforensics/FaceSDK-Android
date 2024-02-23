package com.regula.facesamplekotlin.util

import android.content.Context
import android.widget.Toast
import com.regula.facesdk.model.results.LivenessResponse

object LivenessResponseUtil {
    fun response(context: Context?, livenessResponse: LivenessResponse) {
        if (livenessResponse.exception != null)
            Toast.makeText(context, "Error: " + livenessResponse.exception!!.message, Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Liveness status: " + livenessResponse.liveness.name, Toast.LENGTH_SHORT).show()
    }
}