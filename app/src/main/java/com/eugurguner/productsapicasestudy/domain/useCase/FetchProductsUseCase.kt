package com.eugurguner.productsapicasestudy.domain.useCase

import com.eugurguner.productsapicasestudy.core.sortAndFilter.SortOption
import com.eugurguner.productsapicasestudy.core.sortAndFilter.sortList
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository

class FetchProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(
        sortOption: SortOption,
        limit: Int,
        page: Int
    ): List<Product> {
        val productList = productRepository.fetchProducts(limit = limit, page = page)
        val favoriteProducts = productRepository.getFavoriteProducts()
        productList.forEach { product ->
            if (favoriteProducts.contains(product)) {
                product.isSaved = true
            }
        }
        return sortList(sortOption = sortOption, productList = productList)
    }
}