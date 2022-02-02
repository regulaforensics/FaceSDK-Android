package com.regula.facesamplekotlin.items

import android.content.Context
import com.regula.facesamplekotlin.R
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK
import java.net.HttpURLConnection

class NetworkInterceptorItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        FaceSDK.Instance().setNetworkInterceptorListener { httpURLConnection: HttpURLConnection ->
            httpURLConnection.setRequestProperty("fields", "value")
        }
        FaceSDK.Instance().serviceUrl = FaceSDK.Instance().serviceUrl
        FaceSDK.Instance().startLiveness(context) { }
    }

    override fun getTitle(): Int {
        return R.string.network_interceptor
    }

    override val description: Int
        get() = R.string.network_interceptor_description
}