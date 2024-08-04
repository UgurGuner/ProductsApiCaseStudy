package com.eugurguner.productsapicasestudy.domain.useCase.cart

import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class RemoveProductFromCartUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String) {
        productRepository.removeProductFromCart(productId = productId)
    }
}