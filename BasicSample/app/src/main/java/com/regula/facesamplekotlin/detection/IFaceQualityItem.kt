package com.regula.facesamplekotlin.detection

interface IFaceQualityItem {
    fun getImage(): String?
    fun getTitle(): String
    fun isHeader(): Boolean
}