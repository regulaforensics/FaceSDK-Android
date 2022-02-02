package com.regula.facesamplekotlin.fragments

import android.view.View
import com.regula.facesdk.fragment.FaceDefaultUiFragment

class HideCloseButtonFragment : FaceDefaultUiFragment() {

    override fun getCloseButton(view: View?): View {
        val button: View = super.getCloseButton(view)
        button.visibility = View.INVISIBLE
        return button
    }
}