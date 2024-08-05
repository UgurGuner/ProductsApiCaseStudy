package com.eugurguner.productsapicasestudy.domain.repository

import com.eugurguner.productsapicasestudy.domain.model.Product

interface ProductRepository {
    suspend fun fetchProducts(): List<Product>
    suspend fun getFavoriteProducts(): List<Product>
    suspend fun getCartProducts(): List<Product>
    suspend fun getCartProductById(productId: String): Product?
    suspend fun saveProduct(product: Product)
    suspend fun addProductToCart(product: Product)
    suspend fun removeProduct(productId: String)
    suspend fun removeProductFromCart(productId: String)
    suspend fun increaseCartProductQuantity(product: Product)
    suspend fun decreaseCartProductQuantity(product: Product)
}