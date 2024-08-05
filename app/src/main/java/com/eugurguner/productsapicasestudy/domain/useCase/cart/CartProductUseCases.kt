package com.eugurguner.productsapicasestudy.domain.useCase.cart

data class CartProductUseCases(
    val getCartProductsUseCase: GetCartProductsUseCase,
    val addProductToCartUseCase: AddProductToCartUseCase,
    val decreaseCartProductUseCase: DecreaseCartProductUseCase,
    val increaseCartProductUseCase: IncreaseCartProductUseCase
)