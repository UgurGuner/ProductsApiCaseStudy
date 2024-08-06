package com.eugurguner.productsapicasestudy.data

import com.eugurguner.productsapicasestudy.data.dataSource.ProductDataSource
import com.eugurguner.productsapicasestudy.data.model.ProductDTO
import com.eugurguner.productsapicasestudy.data.network.AppNetworkService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.Test

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductDataSourceTest {
    @Mock
    private lateinit var appNetworkService: AppNetworkService

    private lateinit var productDataSource: ProductDataSource

    @Before
    fun setup() {
        productDataSource = ProductDataSource(appNetworkService)
    }

    @Test
    fun `fetchProducts returns products from network service successfully`() =
        runTest {
            val sampleProductDTOs =
                listOf(
                    ProductDTO(id = "P001", name = "Product A", imageUrl = "", price = 10.0.toString(), description = "Description A", model = "Model A", brand = "Brand A", createdAt = ""),
                    ProductDTO(id = "P002", name = "Product B", imageUrl = "", price = 20.0.toString(), description = "Description B", model = "Model B", brand = "Brand B", createdAt = ""),
                    ProductDTO(id = "P003", name = "Product C", imageUrl = "", price = 30.0.toString(), description = "Description C", model = "Model C", brand = "Brand C", createdAt = "")
                )
            Mockito.`when`(appNetworkService.getProducts(anyInt(), anyInt())).thenReturn(sampleProductDTOs)

            val result = productDataSource.fetchProducts(10, 1)

            Mockito.verify(appNetworkService).getProducts(10, 1)
            assertThat(result).isEqualTo(sampleProductDTOs)
        }

    @Test(expected = RuntimeException::class)
    fun `fetchProducts throws exception when network service fails`() =
        runTest {
            Mockito.`when`(appNetworkService.getProducts(anyInt(), anyInt())).thenThrow(RuntimeException("Network error"))
            productDataSource.fetchProducts(10, 1)
        }
}