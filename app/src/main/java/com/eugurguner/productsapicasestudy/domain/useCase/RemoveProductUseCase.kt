package com.eugurguner.productsapicasestudy.domain.useCase

import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class RemoveProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String) {
        productRepository.removeProduct(productId = productId)
    }
}