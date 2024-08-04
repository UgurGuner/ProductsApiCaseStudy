package com.eugurguner.productsapicasestudy.data.network

import com.eugurguner.productsapicasestudy.data.model.ProductDTO
import retrofit2.http.GET

interface AppNetworkService {
    @GET("products")
    suspend fun getProducts(): List<ProductDTO>
}