package com.eugurguner.productsapicasestudy.domain.model

data class Product(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String
)