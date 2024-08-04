package com.eugurguner.productsapicasestudy.domain.useCase.cart

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class AddProductToCartUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        productRepository.addProductToCart(product = product)
    }
}