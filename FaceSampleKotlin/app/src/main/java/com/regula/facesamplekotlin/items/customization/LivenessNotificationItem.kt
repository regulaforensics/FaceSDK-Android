package com.regula.facesamplekotlin.items.customization

import android.content.Context
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.util.LivenessResponseUtil
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.callback.LivenessNotificationCallback
import com.regula.facesdk.model.LivenessNotification
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class LivenessNotificationItem : CategoryItem() {

    override fun onItemSelected(context: Context) {

        val json = getJsonFromAssets(context, "notification.json")
        FaceSDK.Instance().customization.setUiCustomizationLayer(json)

        FaceSDK.Instance().startLiveness(context,
            { livenessResponse ->
                FaceSDK.Instance().customization.setUiCustomizationLayer(null)
                LivenessResponseUtil.response(context, livenessResponse)

            }, object : LivenessNotificationCallback() {
                override fun onLivenessNotification(livenessNotification: LivenessNotification) {
                    println(livenessNotification.status.name)
                    updateState(json, livenessNotification.status.name)
                    FaceSDK.Instance().customization.setUiCustomizationLayer(json)
                }
            })
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

    private fun updateState(jsonObject: JSONObject?, status: String) {
        if (jsonObject != null) {
            try {
                (jsonObject.getJSONArray("objects")[0] as JSONObject).getJSONObject("label")
                    .put("text", status)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override val title: String
        get() = "Liveness Notification"

    override val description: String
        get() = "Get liveness processing status"
}