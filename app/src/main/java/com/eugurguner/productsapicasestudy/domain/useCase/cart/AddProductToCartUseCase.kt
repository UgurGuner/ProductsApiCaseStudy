package com.eugurguner.productsapicasestudy.domain.useCase.cart

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class AddProductToCartUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        val existingCartItem = productRepository.getCartProductById(productId = product.id)
        if (existingCartItem != null) {
            productRepository.increaseCartProductQuantity(product = existingCartItem)
        } else {
            productRepository.addProductToCart(product = product)
        }
    }
}