package com.regula.facesamplekotlin.items.fragments

import android.graphics.Color
import com.regula.facesdk.fragment.FaceDefaultUiFragment

class ButtonsColorFragment: FaceDefaultUiFragment() {

    override fun getButtonsColor(isLightOn: Boolean): Int {
        return if (isLightOn) Color.RED else Color.GREEN
    }
}