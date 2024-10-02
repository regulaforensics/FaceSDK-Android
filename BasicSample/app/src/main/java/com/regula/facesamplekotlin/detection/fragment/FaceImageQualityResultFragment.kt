package com.regula.facesamplekotlin.detection.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.regula.facesamplekotlin.FaceImageQualityActivity
import com.regula.facesamplekotlin.databinding.FragmentFaceQualityResultBinding
import com.regula.facesamplekotlin.detection.*
import com.regula.facesdk.detection.response.DetectFaceResult
import com.regula.facesdk.detection.response.DetectFacesResponse
import com.regula.facesdk.enums.ImageQualityResultStatus


class FaceImageQualityResultFragment : Fragment() {

    private lateinit var response: DetectFacesResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        response = (activity as FaceImageQualityActivity).response
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFaceQualityResultBinding.inflate(inflater, container, false)

        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = FaceQualityResultAdapter(context, getCategoryData(response.detection))
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        val size = response.allDetections?.size ?: 0

        if (size <= 1)
            binding.buttonNext.visibility = View.GONE

        var num = 0
        binding.buttonNext.text = "Next ${num + 1}/$size"
        binding.buttonNext.setOnClickListener {
            num++
            if (num >= size)
                num = 0
            binding.list.adapter = FaceQualityResultAdapter(
                binding.list.context,
                getCategoryData(response.allDetections?.get(num))
            )

            binding.buttonNext.text = "Next ${num + 1}/$size"
        }

        return binding.root
    }

    private fun getCategoryData(detection: DetectFaceResult?): List<IFaceQualityItem> {
        val list: MutableList<IFaceQualityItem> = ArrayList()
        val groupList = getGroupList(detection)

        detection?.cropImage?.let { list.add(FaceImageResultItem(it)) }
        for (group in groupList) {
            list.add(group)
            val tmpList: MutableList<IFaceQualityItem> = ArrayList()
            for (quality in detection?.quality!!) {
                if (group.id == quality.group.value) {
                    if(quality.status != ImageQualityResultStatus.IMAGE_QUALITY_RESULT_STATUS_TRUE) {
                        tmpList.add(0, FaceQualityResultItem(quality))
                    } else{
                        tmpList.add(FaceQualityResultItem(quality))
                    }
                }
            }
            list.addAll(tmpList)
        }
        return list
    }


    private fun getGroupList(detection: DetectFaceResult?): List<GroupQualityResultItem> {
        val groupList: MutableList<GroupQualityResultItem> = ArrayList()

        val groupIdList: MutableList<Int> = ArrayList()
        for (quality in detection?.quality!!) {
            if (!groupIdList.contains(quality.group.value)) {
                groupIdList.add(quality.group.value)
            }
        }

        for (groupId in groupIdList) {
            var compliantCount = 0
            var totalCount = 0
            for (quality in detection.quality!!) {
                if (quality.group.value == groupId) {
                    totalCount++
                    if (quality.status == ImageQualityResultStatus.IMAGE_QUALITY_RESULT_STATUS_TRUE)
                        compliantCount++
                }
            }
            val groupItem = GroupQualityResultItem(groupId, compliantCount, totalCount)
            groupList.add(groupItem)
        }
        groupList.sortByDescending { (it.totalCount - it.compliantCount) }
        return groupList
    }
}