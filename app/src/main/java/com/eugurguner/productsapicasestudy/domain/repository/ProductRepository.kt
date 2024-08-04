package com.eugurguner.productsapicasestudy.domain.repository

import com.eugurguner.productsapicasestudy.domain.model.Product

interface ProductRepository {
    suspend fun fetchProducts(): List<Product>
    suspend fun getFavoriteProducts(): List<Product>
    suspend fun saveProduct(product: Product)
    suspend fun removeProduct(productId: String)
}