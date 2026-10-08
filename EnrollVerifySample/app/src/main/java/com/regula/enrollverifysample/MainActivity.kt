package com.regula.enrollverifysample

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.regula.enrollverifysample.databinding.ActivityMainBinding
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.EnrollmentConfiguration
import com.regula.facesdk.configuration.VerificationConfiguration
import com.regula.facesdk.exception.InitException
import com.regula.facesdk.model.results.EnrollmentResponse
import com.regula.facesdk.model.results.LivenessResponse
import com.regula.facesdk.model.results.VerificationResponse
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERSON_ENROLLED = "PERSON_ENROLLED"
        private const val PERSON_ID = "PERSON_ID"
        private const val PERSON_NAME = "PERSON_NAME"

        private const val ENROLLED_IMAGE = "enrolled_image.png"
    }

    @Transient
    private lateinit var binding: ActivityMainBinding
    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("prefs", MODE_PRIVATE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            val personId = prefs.getString(PERSON_ID, null)
            verifyButton.isEnabled = personId != null
            if (personId != null) {
                loadEnrolledBitmap()?.let { enrollImage.setImageBitmap(it) }
                setEnrolledInfo(
                    prefs.getBoolean(PERSON_ENROLLED, false),
                    personId,
                    prefs.getString(PERSON_NAME, null)
                )
                deleteButton.isVisible = true
            }
        }
        applyEdgeToEdgeInsets()
        binding.enrollButon.setOnClickListener { onClickEnroll() }
        binding.verifyButton.setOnClickListener { onClickVerify() }
        binding.deleteButton.setOnClickListener { clearData() }
        FaceSDK.Instance().initialize(this) { status: Boolean, e: InitException? ->
            if (!status) {
                Toast.makeText(
                    this@MainActivity,
                    "Init finished with error: " + if (e != null) e.message else "",
                    Toast.LENGTH_LONG
                ).show()
                return@initialize
            }
            Log.d("MainActivity", "FaceSDK init completed successfully")
        }
    }

    private fun onClickEnroll() {
        binding.errorText.text = ""
        binding.verificationInfoText.text = ""
        FaceSDK.Instance().startEnrollment(
            this,
            EnrollmentConfiguration.Builder().build(),
            { livenessResponse: LivenessResponse?, enrollmentResponse: EnrollmentResponse? ->
                if (livenessResponse == null) {
                    binding.errorText.text = "Liveness response is null!"
                    return@startEnrollment
                }
                livenessResponse.exception?.let { error ->
                    binding.errorText.text = error.message.toString()
                }
                livenessResponse.bitmap?.let {
                    binding.enrollImage.setImageBitmap(it)
                    saveEnrolledBitmap(it)
                }

                if (enrollmentResponse == null) {
                    Toast.makeText(this, "Enrollment response is null!", Toast.LENGTH_LONG).show()
                    return@startEnrollment
                }

                enrollmentResponse.run {
                    setEnrolledInfo(isEnrolled, person?.id?.also {
                        binding.verifyButton.isEnabled = true
                        binding.deleteButton.isVisible = true
                    }, person?.name)
                }

                enrollmentResponse.person
                enrollmentResponse.searchPersons

            },
            null
        )
    }

    private fun SpannableStringBuilder.title(title: String, value: String) = bold { append(title) }
        .append(value)
        .append('\n')

    private fun setEnrolledInfo(enrolled: Boolean, personId: String?, personName: String?) {
        prefs.edit {
            binding.infoText.text = buildSpannedString {
                title("Enrolled: ", enrolled.toString())
                putBoolean(PERSON_ENROLLED, enrolled)
                personId?.let {
                    title("Person ID: ", it)
                    putString(PERSON_ID, it)
                }
                personName?.let {
                    title("Person name: ", it)
                    putString(PERSON_NAME, it)
                }
            }
        }
    }

    private fun saveEnrolledBitmap(bitmap: Bitmap): File? {
        val imageFile = File(filesDir, ENROLLED_IMAGE)

        return try {
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            imageFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadEnrolledBitmap(): Bitmap? {
        val imageFile = File(filesDir, ENROLLED_IMAGE)
        return if (imageFile.exists()) {
            BitmapFactory.decodeFile(imageFile.absolutePath)
        } else {
            null
        }
    }

    private fun onClickVerify() {
        binding.errorText.text = ""
        binding.verificationInfoText.text = ""
        FaceSDK.Instance().startVerification(
            this,
            VerificationConfiguration.Builder.withPersonId(prefs.getString(PERSON_ID, null)!!)
                .build(),
            { livenessResponse: LivenessResponse?, verificationResponse: VerificationResponse? ->
                if (livenessResponse == null) {
                    binding.errorText.text = "Liveness response is null!"
                    return@startVerification
                }
                livenessResponse.exception?.let { error ->
                    binding.errorText.text = error.message.toString()
                }

                if (verificationResponse == null) {
                    Toast.makeText(this, "Verification response is null!", Toast.LENGTH_LONG).show()
                    return@startVerification
                }
                binding.verificationInfoText.text = buildSpannedString {
                    title("VERIFICATION INFO", "")
                    title("Verified: ", verificationResponse.isVerified.toString())
                    verificationResponse.match?.let {
                        title("Match:", "Verified: ${it.isVerified}. Similarity: ${it.similarity}")
                    }
                }
            },
            null
        )
    }

    private fun clearData() {
        prefs.edit {
            remove(PERSON_ENROLLED)
            remove(PERSON_ID)
            remove(PERSON_NAME)
        }
        val enrolled = File(filesDir, "$ENROLLED_IMAGE.png")
        if (enrolled.exists()) enrolled.delete()
        binding.verifyButton.isEnabled = false
        binding.errorText.text = ""
        binding.infoText.text = ""
        binding.verificationInfoText.text = ""
        binding.enrollImage.setImageBitmap(null)
        binding.deleteButton.isVisible = false
    }

    private fun applyEdgeToEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            (binding.root.layoutParams as? ViewGroup.MarginLayoutParams)?.also { params ->
                params.setMargins(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
                )
            }?.let {
                binding.root.layoutParams = it
            }
            insets
        }
    }
}