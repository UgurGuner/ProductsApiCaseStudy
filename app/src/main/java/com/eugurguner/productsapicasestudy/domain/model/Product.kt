package com.eugurguner.productsapicasestudy.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String,
    var isSaved: Boolean = false,
    var quantitiy: Int = 1
) : Parcelable