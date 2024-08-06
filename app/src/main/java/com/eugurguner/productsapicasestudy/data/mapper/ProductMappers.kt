package com.eugurguner.productsapicasestudy.data.mapper

import com.eugurguner.productsapicasestudy.data.model.CartProductDTO
import com.eugurguner.productsapicasestudy.data.model.ProductDTO
import com.eugurguner.productsapicasestudy.domain.model.Product

/**
 * Mapper extension functions between domain data classes and data module data classes which has the advantage
 * to fulfill the separation of concerns for the architectural structure
 * By this way domain models do not directly interact with the data source classes
 * @author Ergun Ugur Guner
 */

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
        createdAt = createdAt,
        quantity = quantity
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
        createdAt = createdAt,
        quantity = quantity
    )