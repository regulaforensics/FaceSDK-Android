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
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.regula.facesamplekotlin.FileUtil.getLicense
import com.regula.facesamplekotlin.databinding.ActivityMatchFacesBinding
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.configuration.InitializationConfiguration
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

    private var imageUri: Uri? = null

    private lateinit var imageType1:ImageType
    private lateinit var imageType2:ImageType

    private lateinit var faceBitmaps: ArrayList<Bitmap>

    private lateinit var binding: ActivityMatchFacesBinding

    private var currentImageView: ImageView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchFacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val license = getLicense(this)
        initFaceSdk(license)

        binding.imageView1.layoutParams.height = 400
        binding.imageView2.layoutParams.height = 400

        binding.imageView1.setOnClickListener { showMenu(binding.imageView1, PICK_IMAGE_1) }
        binding.imageView2.setOnClickListener { showMenu(binding.imageView2, PICK_IMAGE_2) }

        binding.buttonMatch.setOnClickListener {
            if (binding.imageView1.drawable != null && binding.imageView2.drawable != null) {
                binding.textViewSimilarity.text = "Processing…"

                matchFaces(getImageBitmap(binding.imageView1), getImageBitmap(binding.imageView2))
                binding.buttonMatch.isEnabled = false
                binding.buttonClear.isEnabled = false
            } else {
                Toast.makeText(
                    this@MatchFacesActivity,
                    "Having both images are compulsory",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.buttonClear.setOnClickListener {
            binding.imageView1.setImageDrawable(null)
            binding.imageView2.setImageDrawable(null)
            binding.textViewSimilarity.text = "Similarity: null"
        }


        binding.buttonClear.setOnClickListener {
            binding.imageView1.setImageDrawable(null)
            binding.imageView2.setImageDrawable(null)
            binding.textViewSimilarity.text = "Similarity:"
            binding.buttonSee.visibility = View.GONE
        }

        binding.buttonSee.setOnClickListener {
            val dialog = ImageDialogFragment()
            dialog.show(supportFragmentManager, "")
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.type_image_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.type1Spinner.adapter = adapter
        }
        binding.type1Spinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                imageType1 = ImageType.PRINTED
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                imageType1 = getGroupSelection(position)
            }
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.type_image_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.type2Spinner.adapter = adapter
        }
        binding.type2Spinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                imageType2 = ImageType.PRINTED
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                imageType2 = getGroupSelection(position)
            }
        }
    }

    private fun initFaceSdk(license: ByteArray?) {
        binding.mainlayout.visibility = View.INVISIBLE
        license?.let {
            val initConfig: InitializationConfiguration = InitializationConfiguration.Builder(license).setLicenseUpdate(true).build()
            FaceSDK.Instance().initialize(this, initConfig) { status, e ->
                binding.progressLayout.visibility = View.INVISIBLE
                if (!status) {
                    Log.d("MainActivity", "FaceSDK error: " + e?.message)
                    Toast.makeText(
                        this@MatchFacesActivity,
                        "Init finished with error: " + if (e != null) e.message else "",
                        Toast.LENGTH_LONG
                    ).show()
                    return@initialize
                }
                binding.mainlayout.visibility = View.VISIBLE
                Log.d("MainActivity", "FaceSDK init succeed ")
                Log.d("MainActivity", "FaceSDK init completed successfully")
            }
        } ?: return
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
                        binding.type1Spinner
                    else //if PICK_IMAGE_2
                        binding.type2Spinner
                    startFaceCaptureActivity(imageView, spinner)
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
            ActivityResultContracts.RequestPermission()
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

        if (resultCode != RESULT_OK)
            return

        imageUri = data?.data
        binding.textViewSimilarity.text = "Similarity:"

        var imageView: ImageView? = null
        var spinner: Spinner? = null

        if (requestCode == PICK_IMAGE_1) {
            imageView = binding.imageView1
            spinner = binding.type1Spinner
        } else if (requestCode == PICK_IMAGE_2) {
            imageView = binding.imageView2
            spinner = binding.type2Spinner
        }

        imageView?.setImageURI(imageUri)

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
        val firstImage = MatchFacesImage(first, imageType1, binding.detectAll1.isChecked)
        val secondImage = MatchFacesImage(second, imageType2, binding.detectAll2.isChecked)
        val matchFacesRequest = MatchFacesRequest(arrayListOf(firstImage, secondImage))

        val crop = OutputImageCrop(
            OutputImageCropAspectRatio.OUTPUT_IMAGE_CROP_ASPECT_RATIO_3X4
        )
        val outputImageParams = OutputImageParams(crop, Color.WHITE)
        matchFacesRequest.outputImageParams = outputImageParams

        FaceSDK.Instance().matchFaces(this@MatchFacesActivity, matchFacesRequest) { matchFacesResponse: MatchFacesResponse ->
            val split = MatchFacesSimilarityThresholdSplit(matchFacesResponse.results, 0.75)
            val similarity = if (split.matchedFaces.size > 0) {
                split.matchedFaces[0].similarity
            } else if (split.unmatchedFaces.size > 0){
                split.unmatchedFaces[0].similarity
            } else {
                null
            }

            val text = similarity?.let {
                "Similarity: " + String.format("%.2f", it * 100) + "%"
            } ?: matchFacesResponse.exception?.let {
                "Similarity: " + it.message
            } ?: "Similarity: "

            binding.textViewSimilarity.text = text

            faceBitmaps = arrayListOf()

            for(matchFaces in matchFacesResponse.detections) {
                for (face in matchFaces.faces)
                    face.crop?.let {
                        faceBitmaps.add(it) }
            }

            val l = faceBitmaps.size
            if (l > 0) {
                binding.buttonSee.text = "Detections ($l)"
                binding.buttonSee.visibility = View.VISIBLE
            } else {
                binding.buttonSee.visibility = View.GONE
            }

            binding.buttonMatch.isEnabled = true
            binding.buttonClear.isEnabled = true
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
