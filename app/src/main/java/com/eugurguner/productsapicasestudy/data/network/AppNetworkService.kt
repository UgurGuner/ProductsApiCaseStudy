package com.eugurguner.productsapicasestudy.data.network

import com.eugurguner.productsapicasestudy.data.model.ProductDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface AppNetworkService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): List<ProductDTO>
}