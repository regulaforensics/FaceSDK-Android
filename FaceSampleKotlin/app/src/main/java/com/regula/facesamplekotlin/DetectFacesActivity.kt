package com.regula.facesamplekotlin

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Size
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.regula.facesamplekotlin.databinding.ActivityDetectFacesBinding
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.detection.request.DetectFacesConfiguration
import com.regula.facesdk.detection.request.DetectFacesRequest
import com.regula.facesdk.detection.request.OutputImageCrop
import com.regula.facesdk.detection.request.OutputImageParams
import com.regula.facesdk.detection.response.DetectFaceResult
import com.regula.facesdk.detection.response.DetectFacesResponse
import com.regula.facesdk.enums.OutputImageCropAspectRatio
import com.regula.facesdk.model.results.FaceCaptureResponse


class DetectFacesActivity : AppCompatActivity() {

    enum class Scenario {
        CROP_ALL, CROP_CENTER, SCENARIO_1, SCENARIO_2
    }

    @Transient
    private lateinit var binding: ActivityDetectFacesBinding
    private lateinit var scenario: Scenario
    private lateinit var response: DetectFacesResponse
    private lateinit var bitmapToDetect: Bitmap

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetectFacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setImage(R.drawable.detect_face1)
        binding.imageViewMain.setOnClickListener { showMenu(binding.imageViewMain) }
        binding.imageViewSample1.setOnClickListener { setImage(R.drawable.detect_face1) }
        binding.imageViewSample2.setOnClickListener { setImage(R.drawable.detect_face2) }
        binding.imageViewSample3.setOnClickListener { setImage(R.drawable.detect_face3) }
        binding.imageViewSample4.setOnClickListener { setImage(R.drawable.detect_face4) }

        binding.button1.setOnClickListener { updateScenario(Scenario.CROP_CENTER, it) }
        binding.button2.setOnClickListener { updateScenario(Scenario.CROP_ALL, it) }
        binding.button3.setOnClickListener { updateScenario(Scenario.SCENARIO_1, it) }
        binding.button4.setOnClickListener { updateScenario(Scenario.SCENARIO_2, it) }

        binding.buttonPick.setOnClickListener {
            showMenu(binding.buttonPick)
        }
        binding.buttonDetect.setOnClickListener {
            when (scenario) {
                Scenario.CROP_CENTER -> detectFacesCropCenter()
                Scenario.CROP_ALL -> detectFacesCropAll()
                Scenario.SCENARIO_1 -> detectFacesScenario1()
                Scenario.SCENARIO_2 -> detectFacesScenario2()
            }
        }
        updateScenario(Scenario.CROP_CENTER, binding.button1)
    }

    private fun setImage(res: Int) {
        val option = BitmapFactory.Options()
        option.inScaled = false
        val bitmap = BitmapFactory.decodeResource(resources, res, option)
        setImage(bitmap)
    }

    private fun setImage(bitmap: Bitmap) {
        bitmapToDetect = bitmap;
        binding.imageViewMain.setImageBitmap(bitmapToDetect);
    }

    private fun updateScenario(scenario: Scenario, view: View) {
        this.scenario = scenario
        binding.button1.setBackgroundColor(0)
        binding.button2.setBackgroundColor(0)
        binding.button3.setBackgroundColor(0)
        binding.button4.setBackgroundColor(0)
        view.setBackgroundResource(R.drawable.rounded_background)
    }

    private fun buttonEnable(enable: Boolean) {
        binding.buttonDetect.isEnabled = enable;
        binding.buttonPick.isEnabled = enable;
    }

    private fun detectFacesCropAll() {
        val request = DetectFacesRequest.cropAllFacesRequestForImage(bitmapToDetect)
        detectFaces(request)
    }

    private fun detectFacesCropCenter() {
        val request = DetectFacesRequest.cropCentralFaceRequestForImage(bitmapToDetect)
        detectFaces(request)
    }

    private fun detectFacesScenario1() {
        val crop = OutputImageCrop(OutputImageCropAspectRatio.OUTPUT_IMAGE_CROP_ASPECT_RATIO_4X5)
        val outputImageParams = OutputImageParams(crop)
        val config = DetectFacesConfiguration()
        config.onlyCentralFace = true
        config.outputImageParams = outputImageParams
        val request = DetectFacesRequest(bitmapToDetect, config)
        detectFaces(request)
    }

    private fun detectFacesScenario2() {
        val size = Size(500, 600)
        val crop = OutputImageCrop(
            OutputImageCropAspectRatio.OUTPUT_IMAGE_CROP_ASPECT_RATIO_2X3,
            size,
            Color.BLACK,
            true
        )
        val outputImageParams = OutputImageParams(crop)
        val config = DetectFacesConfiguration()
        config.onlyCentralFace = false
        config.outputImageParams = outputImageParams
        val request = DetectFacesRequest(bitmapToDetect, config)
        detectFaces(request)
    }

    private fun detectFaces(request: DetectFacesRequest) {
        buttonEnable(false)
        binding.progressBar.visibility = View.VISIBLE;
        FaceSDK.Instance().detectFaces(request) { response: DetectFacesResponse ->
            buttonEnable(true)
            binding.progressBar.visibility = View.GONE;
            response.allDetections?.let {
                drawLandmark(it)
                val size = response.allDetections?.size ?: 0
                binding.buttonSee.text = "See Face Image ($size)"
                binding.buttonSee.visibility = View.VISIBLE
                binding.buttonSee.setOnClickListener(View.OnClickListener {
                    this.response = response
                    val dialog = ImageDialogFragment()
                    dialog.show(supportFragmentManager, "")
                })
            } ?: kotlin.run { binding.buttonSee.visibility = View.GONE }
        }
    }

    private fun drawLandmark(faces: MutableList<DetectFaceResult>) {
        val mutableBitmap: Bitmap =
            bitmapToDetect.copy(bitmapToDetect.config, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.color = Color.GREEN
        paint.strokeWidth = 4f

        for (face in faces) {
            paint.style = Paint.Style.STROKE;
            face.faceRect?.let { canvas.drawRect(it, paint) };

            paint.style = Paint.Style.FILL;
            face.landMarks?.let {
                for (p in face.landMarks!!) {
                    canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), 4F, paint);
                }
            }
        }
        binding.imageViewMain.setImageBitmap(mutableBitmap)
    }

    private fun showMenu(view: View?) {
        val popupMenu = PopupMenu(this@DetectFacesActivity, view)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.gallery -> {
                    openGallery()
                    return@setOnMenuItemClickListener true
                }
                R.id.camera -> {
                    startFaceCaptureActivity()
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

    private fun openGallery() {
        startForResult.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
        )
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                binding.imageViewMain.setImageURI(intent?.data)
                setImage(getImageBitmap(binding.imageViewMain))
            }
        }

    private fun startFaceCaptureActivity() {
        val configuration = FaceCaptureConfiguration.Builder().setCameraSwitchEnabled(true).build()

        FaceSDK.Instance().presentFaceCaptureActivity(
            this@DetectFacesActivity,
            configuration
        ) { faceCaptureResponse: FaceCaptureResponse? ->
            if (faceCaptureResponse?.image != null) {
                faceCaptureResponse.image?.let {
                    setImage(it.bitmap)
                }
            }
        }
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

            val imageView: ImageView =
                rootView.findViewById(R.id.imageViewCrop)
            val buttonNext: Button = rootView.findViewById(R.id.buttonNext)
            val images: List<DetectFaceResult> =
                (activity as DetectFacesActivity).response.allDetections as List<DetectFaceResult>

            if (images.size < 2)
                buttonNext.visibility = View.GONE

            var num = 0
            buttonNext.setOnClickListener {
                num++
                if (num >= images.size)
                    num = 0
                setImage(imageView, images[num].cropImage)
            }

            setImage(imageView, images[0].cropImage)
            return rootView
        }

        private fun setImage(imageView: ImageView, image: String?) {

            image?.let {
                val decodedString: ByteArray = Base64.decode(it, Base64.NO_WRAP)
                imageView.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        decodedString,
                        0,
                        decodedString.size
                    )
                )
            }
        }
    }
}
