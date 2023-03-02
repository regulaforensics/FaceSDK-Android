package com.regula.facesamplekotlin.items.fragments

import android.view.View
import android.widget.TextView
import com.regula.facesamplekotlin.R
import com.regula.facesdk.fragment.FaceUiFragment

class BasicCustomUiFragment: FaceUiFragment() {

    override fun getSwapCameraButton(view: View): View? {
        return null
    }

    override fun getFlashLightButton(view: View): View? {
        return null
    }

    override fun getNotificationView(view: View): TextView? {
        return view.findViewById(R.id.notificationView)
    }

    override fun getResourceLayoutId(): Int {
        return R.layout.custom_hint_view_appearance
    }

    override fun getCloseButton(view: View): View? {
        return view.findViewById(R.id.closeButton)
    }
}