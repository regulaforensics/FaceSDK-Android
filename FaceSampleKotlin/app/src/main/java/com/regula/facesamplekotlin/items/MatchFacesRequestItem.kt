package com.regula.facesamplekotlin.items

import android.content.Context
import android.content.Intent
import com.regula.facesamplekotlin.category.CategoryItem
import com.regula.facesamplekotlin.MatchFacesActivity
import com.regula.facesamplekotlin.R


class MatchFacesRequestItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val intent = Intent(context, MatchFacesActivity::class.java)
        context.startActivity(intent)
    }

    override fun getTitle(): Int {
        return R.string.match_faces_request
    }

    override val description: Int
        get() = R.string.match_faces_request_description

}