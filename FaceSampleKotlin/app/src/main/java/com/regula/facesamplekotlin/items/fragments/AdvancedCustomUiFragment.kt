package com.regula.facesamplekotlin.items.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.regula.facesamplekotlin.R
import com.regula.facesdk.fragment.FaceUiFragment
import com.regula.facesdk.view.NotificationTextView

class AdvancedCustomUiFragment : FaceUiFragment() {

    private var mSwapCameraBtn: ImageButton? = null
    private var mFlashLightBtn: ImageButton? = null
    private var mCloseBtn: ImageButton? = null
    private var mNotificationTextView: NotificationTextView? = null

    override fun getCloseButton(v: View?): View {
        mCloseBtn = v?.findViewById(R.id.exitButton)
        return mCloseBtn!!
    }

    override fun getSwapCameraButton(v: View): View {
        mSwapCameraBtn = v.findViewById(R.id.swapCameraButton)
        return mSwapCameraBtn!!
    }

    override fun getFlashLightButton(v: View): View {
        mFlashLightBtn = v.findViewById(R.id.lightButton)
        return mFlashLightBtn!!
    }

    override fun getNotificationView(v: View): TextView {
        mNotificationTextView = v.findViewById(R.id.notificationTextView)
        return mNotificationTextView!!
    }


    override fun onScreenLightChanged(isScreenLightOn: Boolean) {
        val buttonsColor = getButtonsColor(isScreenLightOn)
        mCloseBtn?.setColorFilter(buttonsColor, PorterDuff.Mode.SRC_ATOP)
        mFlashLightBtn?.setColorFilter(buttonsColor, PorterDuff.Mode.SRC_ATOP)
        mSwapCameraBtn?.setColorFilter(buttonsColor, PorterDuff.Mode.SRC_ATOP)
    }

    override fun getResourceLayoutId(): Int {
        return R.layout.custom_toolbar_appearance;
    }


    private fun getButtonsColor(isLightOn: Boolean): Int {
        return if (isLightOn) Color.BLACK else Color.WHITE
    }
}