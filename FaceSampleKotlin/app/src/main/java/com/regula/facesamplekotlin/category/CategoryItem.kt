package com.regula.facesamplekotlin.category

import android.content.Context


abstract class CategoryItem : ICategoryItem {
    abstract fun onItemSelected(context: Context)
    abstract val description: String

    override fun isHeader(): Boolean {
        return false
    }
}