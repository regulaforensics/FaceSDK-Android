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
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
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
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var imageType1: ImageType
    private lateinit var imageType2: ImageType

    private lateinit var buttonMatch: Button
    private lateinit var buttonClear: Button
    private lateinit var buttonSee: Button

    private lateinit var textViewSimilarity: TextView

    private lateinit var faceBitmaps: ArrayList<Bitmap>

    private var currentImageView: ImageView? = null

    private var imageUri: Uri? = null

    private val photoHelper by lazy { PhotoHelper(this as AppCompatActivity) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_faces)

        imageView1 = findViewById(R.id.imageView1)
        imageView1.layoutParams.height = 400

        imageView2 = findViewById(R.id.imageView2)
        imageView2.layoutParams.height = 400

        switchDetectAll1 = findViewById(R.id.detectAll1)
        switchDetectAll2 = findViewById(R.id.detectAll2)

        buttonMatch = findViewById(R.id.buttonMatch)
        buttonClear = findViewById(R.id.buttonClear)

        textViewSimilarity = findViewById(R.id.textViewSimilarity)

        imageView1.setOnClickListener { showMenu(imageView1, PICK_IMAGE_1) }
        imageView2.setOnClickListener { showMenu(imageView2, PICK_IMAGE_2) }

        buttonMatch.setOnClickListener {
            if (imageView1.drawable != null && imageView2.drawable != null) {
                textViewSimilarity.text = "Processing…"
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
            textViewSimilarity.text = "Similarity:"
            buttonSee.visibility = View.GONE
        }

        buttonSee = findViewById(R.id.buttonSee)
        buttonSee.setOnClickListener {
            val dialog = ImageDialogFragment()
            dialog.show(supportFragmentManager, "")
        }

        spinner1 = findViewById(R.id.type1_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.type_image_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner1.adapter = adapter
        }
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                imageType1 = ImageType.PRINTED
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                imageType1 = getGroupSelection(position)
            }
        }

        spinner2 = findViewById(R.id.type2_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.type_image_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                imageType2 = ImageType.PRINTED
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                imageType2 = getGroupSelection(position)
            }
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
                    val spinner = if (i == PICK_IMAGE_1)
                        spinner1
                    else //if PICK_IMAGE_2
                        spinner2
                    startFaceCaptureActivity(imageView, spinner)
                    return@setOnMenuItemClickListener true
                }

                R.id.photo -> {
                    currentImageView = imageView
                    openDefaultCamera()
                    return@setOnMenuItemClickListener true
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
        photoHelper.makePhoto(startCameraForResult) {}
    }

    private val startCameraForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            currentImageView?.setImageBitmap(photoHelper.handleResult(result.resultCode)!!)
            photoHelper.deleteImageFile()
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

    private fun startFaceCaptureActivity(imageView: ImageView?, spinner: Spinner) {
        val configuration = FaceCaptureConfiguration.Builder().setCameraSwitchEnabled(true).build()

        FaceSDK.Instance().presentFaceCaptureActivity(this@MatchFacesActivity, configuration) { faceCaptureResponse: FaceCaptureResponse? ->
            if (faceCaptureResponse?.image != null) {
                imageView!!.setImageBitmap(faceCaptureResponse.image!!.bitmap)
                spinner.setSelection(2)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK || data == null)
            return

        imageUri = data.data
        textViewSimilarity.text = "Similarity:"

        var imageView: ImageView? = null
        var spinner: Spinner? = null

        if (requestCode == PICK_IMAGE_1) {
            imageView = imageView1
            spinner = spinner1
        } else if (requestCode == PICK_IMAGE_2) {
            imageView = imageView2
            spinner = spinner2
        }

        imageUri?.let {
            val bitmap = contentResolver?.openInputStream(it).use { data ->
                BitmapFactory.decodeStream(data)
            }
            val resizedBitmap = ResizeTransformation(1080).transform(bitmap)
            imageView?.setImageBitmap(resizedBitmap)
        }

        spinner?.setSelection(0)
    }

    private fun matchFaces(first: Bitmap, second: Bitmap) {
        val firstImage = MatchFacesImage(first, imageType1, switchDetectAll1.isChecked)
        val secondImage = MatchFacesImage(second, imageType2, switchDetectAll2.isChecked)
        val matchFacesRequest = MatchFacesRequest(arrayListOf(firstImage, secondImage))

        val crop = OutputImageCrop(
            OutputImageCropAspectRatio.OUTPUT_IMAGE_CROP_ASPECT_RATIO_3X4
        )
        val outputImageParams = OutputImageParams(crop, Color.WHITE)
        matchFacesRequest.outputImageParams = outputImageParams

        FaceSDK.Instance().matchFaces(
            this@MatchFacesActivity,
            matchFacesRequest
        ) { matchFacesResponse: MatchFacesResponse ->
            val split = MatchFacesSimilarityThresholdSplit(matchFacesResponse.results, 0.75)
            val similarity = if (split.matchedFaces.size > 0) {
                split.matchedFaces[0].similarity
            } else if (split.unmatchedFaces.size > 0) {
                split.unmatchedFaces[0].similarity
            } else {
                null
            }

            val text = similarity?.let {
                "Similarity: " + String.format("%.2f", it * 100) + "%"
            } ?: matchFacesResponse.exception?.let {
                "Similarity: " + it.message
            } ?: "Similarity: "

            textViewSimilarity.text = text

            faceBitmaps = arrayListOf()

            for (matchFaces in matchFacesResponse.detections) {
                for (face in matchFaces.faces)
                    face.crop?.let {
                        faceBitmaps.add(it)
                    }
            }

            val l = faceBitmaps.size
            if (l > 0) {
                buttonSee.text = "Detections ($l)"
                buttonSee.visibility = View.VISIBLE
            } else {
                buttonSee.visibility = View.GONE
            }

            buttonMatch.isEnabled = true
            buttonClear.isEnabled = true
        }
    }

    private fun getGroupSelection(index: Int): ImageType {
        when (index) {
            0 -> return ImageType.PRINTED
            1 -> return ImageType.RFID
            2 -> return ImageType.LIVE
            4 -> return ImageType.EXTERNAL
            3 -> return ImageType.DOCUMENT_WITH_LIVE
            5 -> return ImageType.GHOST_PORTRAIT
            6 -> return ImageType.BARCODE
        }
        return ImageType.PRINTED
    }

    companion object {
        private const val PICK_IMAGE_1 = 1
        private const val PICK_IMAGE_2 = 2
    }

    class ImageDialogFragment : DialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(
                R.layout.fragment_image,
                container,
                false
            )
            val faceBitmaps: ArrayList<Bitmap> =
                (activity as MatchFacesActivity).faceBitmaps

            val imageView: ImageView =
                rootView.findViewById(R.id.imageViewCrop)
            val buttonNext: Button = rootView.findViewById(R.id.buttonNext)

            var num = 0
            buttonNext.setOnClickListener {
                num++
                if (num >= faceBitmaps.size)
                    num = 0
                setImage(imageView, faceBitmaps[num])
            }

            setImage(imageView, faceBitmaps[0])
            return rootView
        }

        private fun setImage(imageView: ImageView, image: Bitmap?) {
            image?.let {
                imageView.setImageBitmap(it)
            }
        }
    }
}
