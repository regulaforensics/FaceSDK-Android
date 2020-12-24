package com.regula.facesamplekotlin

import android.annotation.SuppressLint
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
import com.regula.facesdk.FaceReaderService
import com.regula.facesdk.enums.eInputFaceType
import com.regula.facesdk.results.LivenessResponse
import com.regula.facesdk.results.MatchFacesResponse
import com.regula.facesdk.structs.Image
import com.regula.facesdk.structs.MatchFacesRequest


class MainActivity : Activity() {
    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null

    private var buttonMatch: Button? = null
    private var buttonLiveness: Button? = null
    private var buttonClear: Button? = null

    private var textViewSimilarity: TextView? = null
    private var textViewLiveness: TextView? = null

    private var imageUri: Uri? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView1 = findViewById(R.id.imageView1)
        imageView1!!.layoutParams.height = 400

        imageView2 = findViewById(R.id.imageView2)
        imageView2!!.layoutParams.height = 400

        buttonMatch = findViewById(R.id.buttonMatch)
        buttonLiveness = findViewById(R.id.buttonLiveness)
        buttonClear = findViewById(R.id.buttonClear)

        textViewSimilarity = findViewById(R.id.textViewSimilarity)
        textViewLiveness = findViewById(R.id.textViewLiveness)

        imageView1!!.setOnClickListener { v: View? -> showMenu(imageView1, PICK_IMAGE_1) }
        imageView2!!.setOnClickListener { v: View? -> showMenu(imageView2, PICK_IMAGE_2) }

        buttonMatch!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (imageView1!!.drawable != null && imageView2!!.drawable != null) {
                textViewSimilarity!!.text = "Processing..."

                matchFaces(getImageBitmap(imageView1), getImageBitmap(imageView2))
                buttonMatch!!.isEnabled = false
                buttonLiveness!!.isEnabled = false
                buttonClear!!.isEnabled = false
            } else {
                Toast.makeText(this@MainActivity, "Having both images are compulsory", Toast.LENGTH_SHORT).show()
            }
        })

        buttonLiveness!!.setOnClickListener(View.OnClickListener { v: View? -> startLiveness() })

        buttonClear!!.setOnClickListener(View.OnClickListener { v: View? ->
            imageView1!!.setImageDrawable(null)
            imageView2!!.setImageDrawable(null)
            textViewSimilarity!!.text = "Similarity: null"
            textViewLiveness!!.text = "Liveness: null"
        })
    }

    @SuppressLint("ResourceType")
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
        popupMenu.menuInflater.inflate(R.layout.menu, popupMenu.menu)
        popupMenu.show()
    }

    private fun getImageBitmap(imageView: ImageView?): Bitmap {
        imageView!!.invalidate()
        val drawable = imageView.drawable as BitmapDrawable

        return drawable.bitmap
    }

    private fun openGallery(id: Int) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, id)
    }

    private fun startFaceCaptureActivity(imageView: ImageView?) {
        FaceReaderService.Instance().getFaceFromCamera(this@MainActivity) { i: Int, image: Image?, s: String? ->
            if (image != null) {
                imageView!!.setImageBitmap(image.bitmap)
                imageView.tag = eInputFaceType.ift_Live
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_1) {
            imageUri = data.data
            imageView1!!.setImageURI(imageUri)
            imageView1!!.tag = eInputFaceType.ift_DocumentPrinted
            textViewSimilarity!!.text = "Similarity: null"
        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_2) {
            imageUri = data.data
            imageView2!!.setImageURI(imageUri)
            imageView2!!.tag = eInputFaceType.ift_DocumentPrinted
            textViewSimilarity!!.text = "Similarity: null"
        }
    }

    private fun matchFaces(first: Bitmap, second: Bitmap) {
        val matchRequest = MatchFacesRequest()

        val firstImage = Image()
        firstImage.setImage(first)
        firstImage.imageType = (imageView1!!.tag as Int)
        matchRequest.images.add(firstImage)

        val secondImage = Image()
        secondImage.setImage(second)
        secondImage.imageType = (imageView2!!.tag as Int)
        matchRequest.images.add(secondImage)

        FaceReaderService.Instance().matchFaces(matchRequest) { i: Int, matchFacesResponse: MatchFacesResponse?, s: String? ->
            if (matchFacesResponse?.matchedFaces != null) {
                val similarity = matchFacesResponse.matchedFaces[0].similarity
                textViewSimilarity!!.text = "Similarity: " + String.format("%.2f", similarity * 100) + "%"
            } else {
                textViewSimilarity!!.text = "Similarity: null"
            }

            buttonMatch!!.isEnabled = true
            buttonLiveness!!.isEnabled = true
            buttonClear!!.isEnabled = true
        }
    }

    private fun startLiveness() {
        FaceReaderService.Instance().startLivenessMatching(this@MainActivity) { livenessResponse: LivenessResponse? ->
            if (livenessResponse != null && livenessResponse.bitmap != null) {
                imageView1!!.setImageBitmap(livenessResponse.bitmap)
                imageView1!!.tag = eInputFaceType.ift_Live

                if (livenessResponse.liveness == 0) {
                    textViewLiveness!!.text = "Liveness: passed"
                } else {
                    textViewLiveness!!.text = "Liveness: unknown"
                }
            } else {
                textViewLiveness!!.text = "Liveness: null"
            }

            textViewSimilarity!!.text = "Similarity: null"
        }
    }

    companion object {
        private const val PICK_IMAGE_1 = 1
        private const val PICK_IMAGE_2 = 2
    }
}
