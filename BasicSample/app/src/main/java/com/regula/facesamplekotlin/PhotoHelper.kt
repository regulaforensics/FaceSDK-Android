package com.regula.facesamplekotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.regula.facesamplekotlin.util.ResizeTransformation
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoHelper (private val context: Context) {

    private lateinit var photoFile: File
    private lateinit var photoURI: Uri
    private var resultCallback: ((Bitmap?) -> Unit)? = null

    fun makePhoto(activityResultLauncher: ActivityResultLauncher<Intent>, callback: (Bitmap?) -> Unit){
        resultCallback = callback
        photoFile = createImageFile()
        photoURI = FileProvider.getUriForFile(context, "//${context.packageName}.fileprovider", photoFile)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        activityResultLauncher.launch(cameraIntent)
    }

    fun getBitmapImageByUri(uri: Uri): Bitmap{
        val file = createImageFile()

        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return rotateImageIfRequired(file)
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = this.context.filesDir
        return File(storageDir, "temp_photo_${timeStamp}.jpg")
    }

    fun handleResult(resultCode: Int) : Bitmap? {
        return if (resultCode == Activity.RESULT_OK) {
            rotateImageIfRequired(photoFile)
        } else {
            null
        }
    }

    private fun rotateImageIfRequired(imageFile: File): Bitmap {
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        val resizedBitmap = ResizeTransformation(1080).transform(bitmap)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(resizedBitmap!!, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(resizedBitmap!!, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(resizedBitmap!!, 270)
            else -> resizedBitmap!!
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun deleteImageFile(): Boolean {
        return if (photoFile.exists()) {
            photoFile.delete()
        } else {
            false
        }
    }
}