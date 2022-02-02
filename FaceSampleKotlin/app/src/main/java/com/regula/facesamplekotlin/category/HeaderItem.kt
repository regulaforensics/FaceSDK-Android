package com.regula.facesamplekotlin.category

class HeaderItem(private val title: Int) : ICategoryItem {

    override fun getTitle(): Int {
        return title
    }

    override fun isHeader(): Boolean {
        return true
    }
}