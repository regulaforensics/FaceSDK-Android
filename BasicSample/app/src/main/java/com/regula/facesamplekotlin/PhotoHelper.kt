package com.regula.facesamplekotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
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

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = this.context.filesDir
        return File(storageDir, "temp_photo_${timeStamp}.jpg")
    }

    fun handleResult(resultCode: Int) : Bitmap? {
        if (resultCode == Activity.RESULT_OK) {
            val photo = BitmapFactory.decodeFile(photoFile.absolutePath)
            val matrix = Matrix().apply {
                postRotate(90f)
            }
            return Bitmap.createBitmap(photo, 0, 0, photo.width, photo.height, matrix, true)
        } else {
            return null
        }
    }

    fun deleteImageFile(): Boolean {
        return if (photoFile.exists()) {
            photoFile.delete()
        } else {
            false
        }
    }
}