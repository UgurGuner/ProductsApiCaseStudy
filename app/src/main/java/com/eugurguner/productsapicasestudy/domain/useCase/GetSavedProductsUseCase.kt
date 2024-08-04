package com.eugurguner.productsapicasestudy.domain.useCase

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class GetSavedProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): List<Product> = productRepository.getFavoriteProducts()
}