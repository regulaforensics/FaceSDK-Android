package com.regula.facesamplekotlin.util

import android.graphics.Bitmap
import kotlin.math.max

class ResizeTransformation(private val maxSize: Int) {
    fun transform(source: Bitmap?): Bitmap? {
        var result:Bitmap? = null


        if (source != null) {
            var width = source.width
            var height = source.height

            if(max(width, height) <= maxSize)
                return source

            val bitmapRatio = width.toFloat() / height.toFloat()

            if (bitmapRatio > 1) {
                width = maxSize;
                height = (width / bitmapRatio).toInt()
            } else {
                height = maxSize;
                width = (height * bitmapRatio).toInt()
            }

            result = Bitmap.createScaledBitmap(source, width, height, true)
            source.recycle()
        }

        return result
    }
}