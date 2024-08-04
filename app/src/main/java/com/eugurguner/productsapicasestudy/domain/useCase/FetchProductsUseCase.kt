package com.eugurguner.productsapicasestudy.domain.useCase

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class FetchProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): List<Product> {
        val productList = productRepository.fetchProducts()
        val favoriteProducts = productRepository.getFavoriteProducts()
        productList.forEach { product ->
            if (favoriteProducts.contains(product)) {
                product.isSaved = true
            }
        }
        return productList
    }
}