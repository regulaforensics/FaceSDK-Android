package com.regula.facesamplekotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.regula.facesamplekotlin.FileUtil.getLicense
import com.regula.facesamplekotlin.databinding.ActivityMatchFacesBinding
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration
import com.regula.facesdk.configuration.InitializationConfiguration
import com.regula.facesdk.enums.ImageType
import com.regula.facesdk.model.MatchFacesImage
import com.regula.facesdk.model.results.FaceCaptureResponse
import com.regula.facesdk.model.results.matchfaces.MatchFacesResponse
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit
import com.regula.facesdk.request.MatchFacesRequest


class MatchFacesActivity : Activity() {

    private var imageUri: Uri? = null

    private lateinit var binding: ActivityMatchFacesBinding

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
                    val radioGroup: RadioGroup
                    if (i == PICK_IMAGE_1)
                        radioGroup = binding.rbGroup0
                    else  //if PICK_IMAGE_2
                        radioGroup = binding.rbGroup1
                    startFaceCaptureActivity(imageView, radioGroup)
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

    private fun startFaceCaptureActivity(imageView: ImageView?, group: RadioGroup) {
        val configuration = FaceCaptureConfiguration.Builder().setCameraSwitchEnabled(true).build()

        FaceSDK.Instance().presentFaceCaptureActivity(this@MatchFacesActivity, configuration) { faceCaptureResponse: FaceCaptureResponse? ->
            if (faceCaptureResponse?.image != null) {
                imageView!!.setImageBitmap(faceCaptureResponse.image!!.bitmap)

                setGroupSelection(group, ImageType.LIVE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK)
            return

        imageUri = data.data
        binding.textViewSimilarity.text = "Similarity: null"

        var imageView: ImageView? = null
        var group: RadioGroup? = null

        if (requestCode == PICK_IMAGE_1) {
            imageView = binding.imageView1
            group = binding.rbGroup0
        } else if (requestCode == PICK_IMAGE_2) {
            imageView = binding.imageView2
            group = binding.rbGroup1
        }

        imageView?.setImageURI(imageUri)

        setGroupSelection(group, ImageType.PRINTED)
    }

    private fun matchFaces(first: Bitmap, second: Bitmap) {
        val firstImage = MatchFacesImage(first, getGroupSelection(binding.rbGroup0), true)
        val secondImage = MatchFacesImage(second, getGroupSelection(binding.rbGroup1), true)
        val matchFacesRequest = MatchFacesRequest(arrayListOf(firstImage, secondImage));
        FaceSDK.Instance().matchFaces(matchFacesRequest) { matchFacesResponse: MatchFacesResponse ->
            matchFacesResponse.exception?.let {
                val errorBuilder = "Error: ${matchFacesResponse.exception?.message} Details: ${matchFacesResponse.exception?.detailedErrorMessage}"
                binding.textViewSimilarity.text = errorBuilder
            } ?: run {
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

                binding.textViewSimilarity.text = text
            }

            binding.buttonMatch.isEnabled = true
            binding.buttonClear.isEnabled = true
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
