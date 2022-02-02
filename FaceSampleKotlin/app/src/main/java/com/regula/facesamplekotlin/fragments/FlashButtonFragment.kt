package com.regula.facesamplekotlin.fragments

import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.regula.facesamplekotlin.R
import com.regula.facesdk.fragment.FaceDefaultUiFragment

class FlashButtonFragment : FaceDefaultUiFragment() {

    var mFlashLightBtn: View? = null

    override fun getFlashLightButton(view: View): View? {
        mFlashLightBtn = super.getFlashLightButton(view)
        return mFlashLightBtn
    }

    override fun updateFlashLightButton(isLightOn: Boolean) {
        if (mFlashLightBtn == null) return

        if (mFlashLightBtn is ImageButton) (mFlashLightBtn as ImageButton).setImageDrawable(
            context?.let { ContextCompat.getDrawable(it,
                if (isLightOn) R.drawable.flash_light_on else R.drawable.flash_light_off) }
        )
    }
}
