package com.regula.facesamplekotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.regula.common.utils.RegulaLog
import com.regula.facesamplekotlin.R
import com.regula.facesdk.fragment.BaseFaceLivenessProcessingFragment


class LivenessProcessingCustomFragment : BaseFaceLivenessProcessingFragment() {
    private var guidelinesTextView: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = super.onCreateView(inflater, container, savedInstanceState)
            ?: return null
        guidelinesTextView = view.findViewById(R.id.guidelines_text_view)
        return view
    }

    override fun getResourceLayoutId(): Int {
        return R.layout.custom_liveness_processing;
    }

    override fun getRetryButton(view: View): View {
        return view.findViewById(R.id.retry_button)
    }

    override fun getCancelButton(view: View): View {
        return view.findViewById(R.id.cancel_button)
    }

    override fun faceSdkError(stringResourceId: IntArray) {
        var stringResourceId: IntArray? = stringResourceId
        val guidelineTextBuilder = StringBuilder()
        if (stringResourceId == null) {
            stringResourceId = intArrayOf(
                R.string.livenessRetry_text_environment,
                R.string.livenessRetry_text_subject
            )
            RegulaLog.e("Not defined error caught")
        }
        for (strId in stringResourceId) {
            guidelineTextBuilder.append(String.format("- %s\n", getString(strId)))
        }
        guidelinesTextView?.text = guidelineTextBuilder
    }

    override fun getProcessingLayout(v: View): View {
        return v.findViewById(R.id.processing_layout)
    }

    override fun getRetryLayout(v: View): View {
        return v.findViewById(R.id.result_layout)
    }

    override fun getCloseButton(v: View): View {
        return v.findViewById(R.id.close_button)
    }
}