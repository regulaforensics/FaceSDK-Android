package com.regula.facesamplekotlin.items.fragments

import android.view.View
import android.widget.TextView
import com.regula.facesdk.fragment.FaceDefaultUiFragment

class HideNotificationViewFragment : FaceDefaultUiFragment() {

    override fun getNotificationView(view: View): TextView? {
        val textView: TextView = super.getNotificationView(view)
        textView.visibility = View.INVISIBLE
        return textView
    }

    override fun setNotificationStatusText(s: String?) {
        // ignore any new status
    }

}