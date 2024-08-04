package com.eugurguner.productsapicasestudy.domain.useCase

data class ProductUseCases(
    val fetchProductsUseCase: FetchProductsUseCase,
    val getSavedProductsUseCase: GetSavedProductsUseCase,
    val saveProductUseCase: SaveProductUseCase,
    val removeProductUseCase: RemoveProductUseCase
)