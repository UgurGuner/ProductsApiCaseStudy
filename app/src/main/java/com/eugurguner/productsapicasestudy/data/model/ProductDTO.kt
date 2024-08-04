package com.eugurguner.productsapicasestudy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product")
data class ProductDTO(
    @PrimaryKey @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("price") val price: String,
    @SerializedName("description") val description: String,
    @SerializedName("model") val model: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("createdAt") val createdAt: String
)