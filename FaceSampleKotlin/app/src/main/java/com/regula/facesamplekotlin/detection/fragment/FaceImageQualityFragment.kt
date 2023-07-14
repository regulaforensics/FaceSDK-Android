package com.regula.facesamplekotlin.detection.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.regula.facesamplekotlin.FaceImageQualityActivity
import com.regula.facesamplekotlin.R
import com.regula.facesamplekotlin.databinding.FragmentFaceQualityBinding
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.detection.request.*
import com.regula.facesdk.detection.response.DetectFaceResult
import com.regula.facesdk.detection.response.DetectFacesResponse
import com.regula.facesdk.enums.OutputImageCropAspectRatio
import com.regula.facesdk.model.results.FaceCaptureResponse

class FaceImageQualityFragment : Fragment() {

    enum class Scenario {
        ICAO, VISA_USA, VISA_SHENGEN, SCENARIO
    }

    @Transient
    private lateinit var binding: FragmentFaceQualityBinding
    private lateinit var scenario: Scenario
    private lateinit var bitmapToDetect: Bitmap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFaceQualityBinding.inflate(inflater, container, false)
        setImage(R.drawable.face_image_quality1)
        binding.imageViewMain.setOnClickListener { showMenu(binding.imageViewMain) }
        binding.imageViewSample1.setOnClickListener { setImage(R.drawable.face_image_quality1) }
        binding.imageViewSample2.setOnClickListener { setImage(R.drawable.face_image_quality2) }
        binding.imageViewSample3.setOnClickListener { setImage(R.drawable.face_image_quality3) }
        binding.imageViewSample4.setOnClickListener { setImage(R.drawable.face_image_quality4) }

        binding.button1.setOnClickListener { updateScenario(Scenario.ICAO, it) }
        binding.button2.setOnClickListener { updateScenario(Scenario.VISA_USA, it) }
        binding.button3.setOnClickListener { updateScenario(Scenario.VISA_SHENGEN, it) }
        binding.button4.setOnClickListener { updateScenario(Scenario.SCENARIO, it) }

        binding.buttonPick.setOnClickListener {
            showMenu(binding.buttonPick)
        }
        binding.buttonDetect.setOnClickListener {
            when (scenario) {
                Scenario.ICAO -> detectFacesICAO()
                Scenario.VISA_USA -> detectFacesVasaUSA()
                Scenario.VISA_SHENGEN -> detectFacesVisaSchengen()
                Scenario.SCENARIO -> detectFacesScenario()
            }
        }
        updateScenario(Scenario.ICAO, binding.button1)
        return binding.root
    }

    private fun setImage(res: Int) {
        val option = BitmapFactory.Options()
        option.inScaled = false
        val bitmap = BitmapFactory.decodeResource(resources, res, option)
        bitmapToDetect = bitmap;
        binding.imageViewMain.setImageResource(res)
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


    private fun detectFacesICAO() {
        val request = DetectFacesRequest.qualityICAORequestForImage(bitmapToDetect)
        detectFaces(request)
    }

    private fun detectFacesVasaUSA() {
        val request = DetectFacesRequest.qualityVisaUSARequestForImage(bitmapToDetect)
        detectFaces(request)
    }

    private fun detectFacesVisaSchengen() {
        val request = DetectFacesRequest.qualityVisaSchengenRequestForImage(bitmapToDetect)
        detectFaces(request)
    }

    private fun detectFacesScenario() {
        val crop = OutputImageCrop(
            OutputImageCropAspectRatio.OUTPUT_IMAGE_CROP_ASPECT_RATIO_4X5,
        )
        val outputImageParams = OutputImageParams(crop, Color.WHITE)
        val config = DetectFacesConfiguration()
        config.onlyCentralFace = false
        config.outputImageParams = outputImageParams
        config.customQuality = listOf(
            ImageQualityGroup.ImageCharacteristic.imageHeightWithRange(600.0, 800.0),
            ImageQualityGroup.ImageCharacteristic.imageWidthWithRange(800.0, 1000.0),
            ImageQualityGroup.FaceImageQuality.blurLevel(),
            ImageQualityGroup.HeadOcclusion.headphones().withCustomRange(0.0, 0.1),
            ImageQualityGroup.HeadOcclusion.headCovering().withCustomRange(0.0, 0.1),
            ImageQualityGroup.QualityBackground.backgroundColorMatchWithColor(Color.WHITE)
        )
        val request = DetectFacesRequest(bitmapToDetect, config)
        detectFaces(request)
    }

    private fun buttonEnable(enable: Boolean) {
        binding.buttonDetect.isEnabled = enable;
        binding.buttonPick.isEnabled = enable;
    }

    private fun detectFaces(request: DetectFacesRequest) {
        buttonEnable(false)
        binding.textResult.text = ""
        binding.progressBar.visibility = View.VISIBLE;
        FaceSDK.Instance().serviceUrl = "https://faceapi.regulaforensics.com/"
        FaceSDK.Instance().detectFaces(request) { response: DetectFacesResponse ->
            binding.progressBar.visibility = View.GONE;
            buttonEnable(true)
            response.allDetections?.let {
                drawLandmark(it)
            }
            response.detection?.let {
                if (it.isQualityCompliant) {
                    binding.textResult.text = "✅ COMPLIANT"
                } else {
                    binding.textResult.text = "❌ NON-COMPLIANT"
                }
                val size = it.quality?.size ?: 0
                binding.buttonSee.text = "Quality Results ($size)"
                binding.buttonSee.visibility = View.VISIBLE
                binding.buttonSee.setOnClickListener { _ ->
                    (activity as FaceImageQualityActivity).response = response
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.add(R.id.container, FaceImageQualityResultFragment())?.addToBackStack("")
                        ?.commit()
                }
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
        val popupMenu = PopupMenu(activity, view)
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

        activity?.let {
            FaceSDK.Instance().presentFaceCaptureActivity(
                it,
                configuration
            ) { faceCaptureResponse: FaceCaptureResponse? ->
                if (faceCaptureResponse?.image != null) {
                    faceCaptureResponse.image?.let { it ->
                        setImage(it.bitmap)
                    }
                }
            }
        }
    }
}