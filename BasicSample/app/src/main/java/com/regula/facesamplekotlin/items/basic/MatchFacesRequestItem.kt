package com.regula.facesamplekotlin.items.basic

import android.content.Context
import android.content.Intent
import com.regula.facesamplekotlin.MatchFacesActivity
import com.regula.facesamplekotlin.category.CategoryItem

class MatchFacesRequestItem : CategoryItem() {

    override fun onItemSelected(context: Context) {
        val intent = Intent(context, MatchFacesActivity::class.java)
        context.startActivity(intent)
    }

    override val title: String
        get() =  "MatchFacesRequest"

    override val description: String
        get() = "Checks the likelihood that two faces belong to the same person"

}