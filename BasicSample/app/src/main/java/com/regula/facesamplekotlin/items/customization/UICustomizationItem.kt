package com.regula.facesamplekotlin.items.customization

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.animation.AccelerateInterpolator
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.util.LivenessResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.LivenessConfiguration
import com.regula.facesdk.enums.ButtonTag
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class UICustomizationItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val json = getJsonFromAssets(context, "layer.json")

        val mTimerAnimator = ValueAnimator.ofFloat(0.9f, 0.1f)

        val configuration = LivenessConfiguration.Builder()
            .setCloseButtonEnabled(false)
            .build()

        FaceSDK.Instance().customization.setUiCustomizationLayer(json)

        FaceSDK.Instance().setOnClickListener { view ->
            when (view.tag as Int) {
                ButtonTag.CLOSE -> {
                    mTimerAnimator?.cancel()
                }
            }
        }
        FaceSDK.Instance().startLiveness(context, configuration) { livenessResponse ->
            mTimerAnimator?.cancel()
            LivenessResponseUtil.response(context, livenessResponse)
        }
        initAnimation(json, mTimerAnimator)
    }


    private fun getJsonFromAssets(context: Context, name: String): JSONObject? {
        try {
            val inputStream = context.assets.open(name)
            val jsonString = Scanner(inputStream).useDelimiter("\\A").next()
            return JSONObject(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun initAnimation(jsonObject: JSONObject?, mTimerAnimator: ValueAnimator) {
        mTimerAnimator.duration = 1000
        mTimerAnimator.interpolator = AccelerateInterpolator()
        mTimerAnimator.repeatMode = ValueAnimator.REVERSE
        mTimerAnimator.repeatCount = 20
        mTimerAnimator.addUpdateListener {
            updatePosition(jsonObject, it.animatedValue as Float)
            FaceSDK.Instance().customization.setUiCustomizationLayer(jsonObject)
        }
        mTimerAnimator.start()
        mTimerAnimator.addListener(object : ValueAnimator(), Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                FaceSDK.Instance().customization.setUiCustomizationLayer(null)
            }

            override fun onAnimationCancel(animation: Animator) {
                FaceSDK.Instance().customization.setUiCustomizationLayer(null)
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }

    private fun updatePosition(jsonObject: JSONObject?, position: Float) {
        if (jsonObject != null) {
            try {
                (jsonObject.getJSONArray("objects")[3] as JSONObject).getJSONObject("image")
                    .put("alpha", position)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override val title: String
        get() = "Custom UI layer"

    override val description: String
        get() = "Custom labels, images and buttons to the camera screen"
}