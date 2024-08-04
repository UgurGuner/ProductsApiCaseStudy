package com.eugurguner.productsapicasestudy.data.dataSource

import com.eugurguner.productsapicasestudy.data.model.ProductDTO
import com.eugurguner.productsapicasestudy.data.network.AppNetworkService

class ProductDataSource(
    private val appNetworkService: AppNetworkService
) {
    suspend fun fetchProducts(): List<ProductDTO> = appNetworkService.getProducts()
}