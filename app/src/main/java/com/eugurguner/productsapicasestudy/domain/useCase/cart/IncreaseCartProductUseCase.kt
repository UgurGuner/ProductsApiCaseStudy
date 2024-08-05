package com.eugurguner.productsapicasestudy.domain.useCase.cart

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class IncreaseCartProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        if (product.quantitiy >= 1) {
            productRepository.increaseCartProductQuantity(product = product)
        } else {
            productRepository.addProductToCart(product = product)
        }
    }
}