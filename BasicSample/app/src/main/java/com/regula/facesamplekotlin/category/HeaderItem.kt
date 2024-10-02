package com.regula.facesamplekotlin.category

class HeaderItem(override val title: String) : ICategoryItem {

    override fun isHeader(): Boolean {
        return true
    }
}