package com.regula.facesamplekotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.enums.ImageType
import com.regula.facesdk.enums.LivenessStatus
import com.regula.facesdk.model.Image
import com.regula.facesdk.model.results.FaceCaptureResponse
import com.regula.facesdk.model.results.LivenessResponse
import com.regula.facesdk.model.results.MatchFacesResponse
import com.regula.facesdk.request.MatchFacesRequest


class MainActivity : Activity() {
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView

    private lateinit var buttonMatch: Button
    private lateinit var buttonLiveness: Button
    private lateinit var buttonClear: Button

    private lateinit var textViewSimilarity: TextView
    private lateinit var textViewLiveness: TextView

    private var imageUri: Uri? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView1 = findViewById(R.id.imageView1)
        imageView1.layoutParams.height = 400

        imageView2 = findViewById(R.id.imageView2)
        imageView2.layoutParams.height = 400

        buttonMatch = findViewById(R.id.buttonMatch)
        buttonLiveness = findViewById(R.id.buttonLiveness)
        buttonClear = findViewById(R.id.buttonClear)

        textViewSimilarity = findViewById(R.id.textViewSimilarity)
        textViewLiveness = findViewById(R.id.textViewLiveness)

        imageView1.setOnClickListener { showMenu(imageView1, PICK_IMAGE_1) }
        imageView2.setOnClickListener { showMenu(imageView2, PICK_IMAGE_2) }

        buttonMatch.setOnClickListener {
            if (imageView1.drawable != null && imageView2.drawable != null) {
                textViewSimilarity.text = "Processing..."

                matchFaces(getImageBitmap(imageView1), getImageBitmap(imageView2))
                buttonMatch.isEnabled = false
                buttonLiveness.isEnabled = false
                buttonClear.isEnabled = false
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Having both images are compulsory",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        buttonLiveness.setOnClickListener { startLiveness() }

        buttonClear.setOnClickListener {
            imageView1.setImageDrawable(null)
            imageView2.setImageDrawable(null)
            textViewSimilarity.text = "Similarity: null"
            textViewLiveness.text = "Liveness: null"
        }
    }

    private fun showMenu(imageView: ImageView?, i: Int) {
        val popupMenu = PopupMenu(this@MainActivity, imageView)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.gallery -> {
                    openGallery(i)
                    return@setOnMenuItemClickListener true
                }
                R.id.camera -> {
                    startFaceCaptureActivity(imageView)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.show()
    }

    private fun getImageBitmap(imageView: ImageView?): Bitmap {
        imageView?.invalidate()
        val drawable = imageView?.drawable as BitmapDrawable

        return drawable.bitmap
    }

    private fun openGallery(id: Int) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, id)
    }

    private fun startFaceCaptureActivity(imageView: ImageView?) {
        val configuration = FaceCaptureConfiguration.Builder().setCameraSwitchEnabled(true).build()

        FaceSDK.Instance().presentFaceCaptureActivity(this@MainActivity, configuration) { faceCaptureResponse: FaceCaptureResponse? ->
            if (faceCaptureResponse?.image != null) {
                imageView!!.setImageBitmap(faceCaptureResponse.image!!.bitmap)
                imageView.tag = ImageType.IMAGE_TYPE_LIVE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK)
            return

        imageUri = data.data
        textViewSimilarity.text = "Similarity: null"

        var imageView: ImageView? = null
        if (requestCode == PICK_IMAGE_1)
            imageView = imageView1;
        else if (requestCode == PICK_IMAGE_2)
            imageView = imageView2;

        imageView?.setImageURI(imageUri)
        imageView?.tag = ImageType.IMAGE_TYPE_PRINTED
    }

    private fun matchFaces(first: Bitmap, second: Bitmap) {
        val firstImage = Image(imageView1.tag as Int, first)
        val secondImage = Image(imageView2.tag as Int, second)

        FaceSDK.Instance().matchFaces(MatchFacesRequest(arrayListOf(firstImage, secondImage))) { matchFacesResponse: MatchFacesResponse ->
            if (matchFacesResponse.matchedFaces.size > 0) {
                val similarity = matchFacesResponse.matchedFaces[0].similarity
                textViewSimilarity.text = "Similarity: " + String.format("%.2f", similarity * 100) + "%"
            } else {
                textViewSimilarity.text = "Similarity: null"
            }

            buttonMatch.isEnabled = true
            buttonLiveness.isEnabled = true
            buttonClear.isEnabled = true
        }
    }

    private fun startLiveness() {
        val configuration = LivenessConfiguration.Builder().setCameraSwitchEnabled(true).build()

        FaceSDK.Instance().startLiveness(this@MainActivity, configuration) { livenessResponse: LivenessResponse ->
            if (livenessResponse.bitmap != null) {
                imageView1.setImageBitmap(livenessResponse.bitmap)
                imageView1.tag = ImageType.IMAGE_TYPE_LIVE

                if (livenessResponse.liveness == LivenessStatus.PASSED) {
                    textViewLiveness.text = "Liveness: passed"
                } else {
                    textViewLiveness.text = "Liveness: unknown"
                }
            } else {
                textViewLiveness.text = "Liveness: null"
            }

            textViewSimilarity.text = "Similarity: null"
        }
    }

    companion object {
        private const val PICK_IMAGE_1 = 1
        private const val PICK_IMAGE_2 = 2
    }
}
