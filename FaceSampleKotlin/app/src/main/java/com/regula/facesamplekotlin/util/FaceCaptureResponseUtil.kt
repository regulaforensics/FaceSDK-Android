package com.regula.facesamplekotlin.util

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.regula.facesdk.model.results.FaceCaptureResponse

object FaceCaptureResponseUtil {
    fun response(context: Context?, faceCaptureResponse: FaceCaptureResponse) {
        if (faceCaptureResponse.exception != null)
            Toast.makeText(
                context,
                "Error: " + faceCaptureResponse.exception!!.message,
                Toast.LENGTH_SHORT
            ).show()
        else {
            if (context is AppCompatActivity) {
                faceCaptureResponse.image?.let {
                    val image = ImageView(context)
                    image.setImageBitmap(it.bitmap)

                    AlertDialog.Builder(context)
                        .setMessage("Face Capture")
                        .setPositiveButton("OK", null)
                        .setView(image).create().show()
                }
            }
            Toast.makeText(context, "Successfully got an image", Toast.LENGTH_SHORT).show()
        }
    }
}