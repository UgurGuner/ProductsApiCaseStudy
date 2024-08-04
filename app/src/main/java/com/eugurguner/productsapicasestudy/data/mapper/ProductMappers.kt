package com.eugurguner.productsapicasestudy.data.mapper

import com.eugurguner.productsapicasestudy.data.model.CartProductDTO
import com.eugurguner.productsapicasestudy.data.model.ProductDTO
import com.eugurguner.productsapicasestudy.domain.model.Product

fun ProductDTO.toDomainModel(): Product =
    Product(
        id = id,
        name = name,
        imageUrl = imageUrl,
        price = price.toDouble(),
        description = description,
        model = model,
        brand = brand,
        createdAt = createdAt
    )

fun Product.toDTO(): ProductDTO =
    ProductDTO(
        id = id,
        name = name,
        imageUrl = imageUrl,
        price = price.toString(),
        description = description,
        model = model,
        brand = brand,
        createdAt = createdAt
    )

fun CartProductDTO.toDomainModel(): Product =
    Product(
        id = id,
        name = name,
        imageUrl = imageUrl,
        price = price.toDouble(),
        description = description,
        model = model,
        brand = brand,
        createdAt = createdAt
    )

fun Product.toCartDTO(): CartProductDTO =
    CartProductDTO(
        id = id,
        name = name,
        imageUrl = imageUrl,
        price = price.toString(),
        description = description,
        model = model,
        brand = brand,
        createdAt = createdAt
    )