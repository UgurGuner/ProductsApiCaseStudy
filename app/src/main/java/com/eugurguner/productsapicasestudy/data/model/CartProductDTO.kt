package com.eugurguner.productsapicasestudy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_product")
data class CartProductDTO(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String,
    val price: String,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String
)