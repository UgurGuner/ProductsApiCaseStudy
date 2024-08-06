package com.eugurguner.productsapicasestudy.domain.fake

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class FakeProductRepository : ProductRepository {
    private val cartList = MutableSharedFlow<List<Product>>()
    private val favoriteProducts = MutableSharedFlow<List<Product>>()
    private val allProducts = MutableSharedFlow<List<Product>>()
    private val cartProductById = MutableSharedFlow<Product?>()
    private val isProcessCompleted = MutableSharedFlow<Boolean>()
    override suspend fun fetchProducts(limit: Int, page: Int): List<Product> = allProducts.first()

    override suspend fun getFavoriteProducts(): List<Product> = favoriteProducts.first()

    override suspend fun getCartProducts(): List<Product> = cartList.first()

    override suspend fun getCartProductById(productId: String): Product? = cartProductById.first()

    override suspend fun saveProduct(product: Product) {
        isProcessCompleted.first()
    }

    override suspend fun addProductToCart(product: Product) {
        isProcessCompleted.first()
    }

    override suspend fun removeProduct(productId: String) {
        isProcessCompleted.first()
    }

    override suspend fun removeProductFromCart(productId: String) {
        isProcessCompleted.first()
    }

    override suspend fun increaseCartProductQuantity(product: Product) {
        isProcessCompleted.first()
    }

    override suspend fun decreaseCartProductQuantity(product: Product) {
        isProcessCompleted.first()
    }

    // Functions to emit values for testing
    suspend fun emitCartList(list: List<Product>) {
        cartList.emit(list)
    }

    suspend fun emitFavoriteProducts(list: List<Product>) {
        favoriteProducts.emit(list)
    }

    suspend fun emitAllProducts(list: List<Product>) {
        allProducts.emit(list)
    }

    suspend fun emitCardProductById(product: Product) {
        cartProductById.emit(product)
    }

    suspend fun emitIsProcessCompleted() {
        isProcessCompleted.emit(true)
    }
}