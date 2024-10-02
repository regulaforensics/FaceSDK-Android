package com.regula.facesamplekotlin.items.fragments

import android.view.View
import android.widget.ImageButton
import com.regula.facesamplekotlin.R
import com.regula.facesdk.fragment.FaceDefaultUiFragment

class SwapButtonFragment : FaceDefaultUiFragment() {

    override fun getSwapCameraButton(v: View): View? {
        val swapCameraBtn = super.getSwapCameraButton(v)
        if (swapCameraBtn is ImageButton) {
            swapCameraBtn.setImageResource(R.drawable.swap_button)
        }
        return swapCameraBtn
    }
}