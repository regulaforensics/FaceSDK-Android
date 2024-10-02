package com.regula.facesamplekotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.regula.facesamplekotlin.util.ResizeTransformation
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.detection.request.OutputImageCrop
import com.regula.facesdk.detection.request.OutputImageParams
import com.regula.facesdk.enums.ImageType
import com.regula.facesdk.enums.OutputImageCropAspectRatio
import com.regula.facesdk.model.MatchFacesImage
import com.regula.facesdk.model.results.FaceCaptureResponse
import com.regula.facesdk.model.results.matchfaces.MatchFacesResponse
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit
import com.regula.facesdk.request.MatchFacesRequest


class MatchFacesActivity : AppCompatActivity() {
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var switchDetectAll1: SwitchCompat
    private lateinit var switchDetectAll2: SwitchCompat
    private lateinit var imageViewResult1: ImageView
    private lateinit var imageViewResult2: ImageView
    private lateinit var group0: RadioGroup
    private lateinit var group1:RadioGroup

    private lateinit var buttonMatch: Button
    private lateinit var buttonClear: Button

    private lateinit var textViewSimilarity: TextView

    private var currentImageView: ImageView? = null

    private var imageUri: Uri? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_faces)

        imageView1 = findViewById(R.id.imageView1)
        imageView1.layoutParams.height = 400

        imageView2 = findViewById(R.id.imageView2)
        imageView2.layoutParams.height = 400

        imageViewResult1 = findViewById(R.id.imageViewResult1)
        imageViewResult2 = findViewById(R.id.imageViewResult2)

        switchDetectAll1 = findViewById(R.id.detectAll1)
        switchDetectAll2 = findViewById(R.id.detectAll2)

        group0 = findViewById(R.id.rbGroup0)
        group1 = findViewById(R.id.rbGroup1)

        buttonMatch = findViewById(R.id.buttonMatch)
        buttonClear = findViewById(R.id.buttonClear)

        textViewSimilarity = findViewById(R.id.textViewSimilarity)

        imageView1.setOnClickListener { showMenu(imageView1, PICK_IMAGE_1) }
        imageView2.setOnClickListener { showMenu(imageView2, PICK_IMAGE_2) }

        buttonMatch.setOnClickListener {
            if (imageView1.drawable != null && imageView2.drawable != null) {
                textViewSimilarity.text = "Processing…"
                imageViewResult1.setImageBitmap(null)
                imageViewResult2.setImageBitmap(null)
                matchFaces(getImageBitmap(imageView1), getImageBitmap(imageView2))
                buttonMatch.isEnabled = false
                buttonClear.isEnabled = false
            } else {
                Toast.makeText(
                    this@MatchFacesActivity,
                    "Having both images are compulsory",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        buttonClear.setOnClickListener {
            imageView1.setImageDrawable(null)
            imageView2.setImageDrawable(null)
            imageViewResult1.setImageDrawable(null)
            imageViewResult2.setImageDrawable(null)
            textViewSimilarity.text = "Similarity: null"
        }
    }

    private fun showMenu(imageView: ImageView?, i: Int) {
        val popupMenu = PopupMenu(this@MatchFacesActivity, imageView)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.gallery -> {
                    openGallery(i)
                    return@setOnMenuItemClickListener true
                }
                R.id.camera -> {
                    val radioGroup = if (i == PICK_IMAGE_1)
                        group0
                    else //if PICK_IMAGE_2
                        group1
                    startFaceCaptureActivity(imageView, radioGroup)
                    return@setOnMenuItemClickListener true
                }
                R.id.photo -> {
                    currentImageView = imageView
                    openDefaultCamera()
                    return@setOnMenuItemClickListener  true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.show()
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                launchCamera()
            } else {
                Toast.makeText(
                    this@MatchFacesActivity,
                    "Camera permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun openDefaultCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this@MatchFacesActivity,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    private fun launchCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startCameraForResult.launch(cameraIntent)
    }

    private val startCameraForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val photo = result.data?.extras?.get("data")
            if (photo is Bitmap)
                currentImageView?.setImageBitmap(photo);
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

    private fun startFaceCaptureActivity(imageView: ImageView?, group: RadioGroup) {
        val configuration = FaceCaptureConfiguration.Builder().setCameraSwitchEnabled(true).build()

        FaceSDK.Instance().presentFaceCaptureActivity(this@MatchFacesActivity, configuration) { faceCaptureResponse: FaceCaptureResponse? ->
            if (faceCaptureResponse?.image != null) {
                imageView!!.setImageBitmap(faceCaptureResponse.image!!.bitmap)

                setGroupSelection(group, ImageType.LIVE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK || data == null)
            return

        imageUri = data.data
        textViewSimilarity.text = "Similarity: null"

        var imageView: ImageView? = null
        var group: RadioGroup? = null

        if (requestCode == PICK_IMAGE_1) {
            imageView = imageView1
            group = group0
        } else if (requestCode == PICK_IMAGE_2) {
            imageView = imageView2
            group = group1
        }

        imageUri?.let {
            val bitmap = contentResolver?.openInputStream(it).use { data ->
                BitmapFactory.decodeStream(data)
            }
            val resizedBitmap = ResizeTransformation(1080).transform(bitmap)
            imageView?.setImageBitmap(resizedBitmap)
        }

        setGroupSelection(group, ImageType.PRINTED)
    }

    private fun matchFaces(first: Bitmap, second: Bitmap) {
        val firstImage = MatchFacesImage(first, getGroupSelection(group0), switchDetectAll1.isChecked)
        val secondImage = MatchFacesImage(second, getGroupSelection(group1), switchDetectAll2.isChecked)
        val matchFacesRequest = MatchFacesRequest(arrayListOf(firstImage, secondImage))

        val crop = OutputImageCrop(
            OutputImageCropAspectRatio.OUTPUT_IMAGE_CROP_ASPECT_RATIO_3X4
        )
        val outputImageParams = OutputImageParams(crop, Color.WHITE)
        matchFacesRequest.outputImageParams = outputImageParams

        FaceSDK.Instance().matchFaces(matchFacesRequest) { matchFacesResponse: MatchFacesResponse ->
            val split = MatchFacesSimilarityThresholdSplit(matchFacesResponse.results, 0.75)
            val similarity = if (split.matchedFaces.size > 0) {
                split.matchedFaces[0].similarity
            } else if (split.unmatchedFaces.size > 0){
                split.unmatchedFaces[0].similarity
            } else {
                null
            }

            val text = similarity?.let {
                "Similarity: " +  String.format("%.2f", it * 100) + "%"
            } ?: "Similarity: null"

            textViewSimilarity.text = text

            if (matchFacesResponse.detections.size > 0 && matchFacesResponse.detections[0] != null
                && matchFacesResponse.detections[0].faces.size > 0
                && matchFacesResponse.detections[0].faces[0].crop != null
            ) {
                imageViewResult1.setImageBitmap(matchFacesResponse.detections[0].faces[0].crop)
            }
            if (matchFacesResponse.detections.size > 1 && matchFacesResponse.detections[1] != null
                && matchFacesResponse.detections[1].faces.size > 0
                && matchFacesResponse.detections[1].faces[0].crop != null
            ) {
                imageViewResult2.setImageBitmap(matchFacesResponse.detections[1].faces[0].crop)
            }

            buttonMatch.isEnabled = true
            buttonClear.isEnabled = true
        }
    }

    private fun getGroupSelection(group: RadioGroup): ImageType? {
        val button = group.findViewById<RadioButton>(group.checkedRadioButtonId)
        val index = group.indexOfChild(button)
        when (index) {
            2 -> return ImageType.LIVE
            1 -> return ImageType.RFID
            0 -> return ImageType.PRINTED
            4 -> return ImageType.EXTERNAL
            3 -> return ImageType.DOCUMENT_WITH_LIVE
            5 -> return ImageType.GHOST_PORTRAIT
            6 -> return ImageType.BARCODE
        }
        return ImageType.PRINTED
    }

    private fun setGroupSelection(group: RadioGroup?, type: ImageType) {
        when (type) {
            ImageType.LIVE -> group?.check(group.getChildAt(2).id)
            ImageType.RFID -> group?.check(group.getChildAt(1).id)
            ImageType.PRINTED -> group?.check(group.getChildAt(0).id)
            ImageType.EXTERNAL -> group?.check(group.getChildAt(4).id)
            ImageType.DOCUMENT_WITH_LIVE -> group?.check(group.getChildAt(3).id)
            ImageType.GHOST_PORTRAIT -> group?.check(group.getChildAt(5).id)
            ImageType.BARCODE -> group?.check(group.getChildAt(6).id)
        }
    }

    companion object {
        private const val PICK_IMAGE_1 = 1
        private const val PICK_IMAGE_2 = 2
    }
}
