package com.regula.facesamplekotlin.items.other

import android.content.Context
import com.regula.common.http.HttpRequestBuilder
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesdk.FaceSDK

class NetworkInterceptorItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        FaceSDK.Instance().setNetworkInterceptorListener { httpRequestBuilder: HttpRequestBuilder ->
            httpRequestBuilder.connection.setRequestProperty("fields", "value")
        }
        FaceSDK.Instance().serviceUrl = FaceSDK.Instance().serviceUrl
        FaceSDK.Instance().startLiveness(context) { }
    }

    override val title: String
        get() =  "Network interceptor"

    override val description: String
        get() = "Adds custom http fields to the outgoing requests"
}