package com.eugurguner.productsapicasestudy.domain.useCase.cart

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class DecreaseCartProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        if (product.quantitiy > 1) {
            productRepository.decreaseCartProductQuantity(product = product)
        } else {
            productRepository.removeProductFromCart(productId = product.id)
        }
    }
}