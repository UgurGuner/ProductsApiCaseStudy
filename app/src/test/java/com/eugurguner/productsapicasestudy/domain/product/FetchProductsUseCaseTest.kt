package com.eugurguner.productsapicasestudy.domain.product

import com.eugurguner.productsapicasestudy.core.sortAndFilter.SortOption
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.FetchProductsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.Test

class FetchProductsUseCaseTest {
    @Mock
    private lateinit var productRepository: ProductRepository
    private lateinit var fetchProductsUseCase: FetchProductsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        fetchProductsUseCase = FetchProductsUseCase(productRepository)
    }

    private val product1 =
        Product(
            id = "1",
            name = "Product A",
            imageUrl = "image_url",
            price = 10.0,
            description = "Description",
            model = "Model A",
            brand = "Brand A",
            createdAt = "2024-08-05T14:52:00Z",
            isSaved = true
        )

    private val product2 =
        Product(
            id = "2",
            name = "Product A",
            imageUrl = "image_url",
            price = 10.0,
            description = "Description",
            model = "Model A",
            brand = "Brand A",
            createdAt = "2024-08-04T14:52:00Z",
            isSaved = false
        )

    @Test
    fun `invoke fetches products, marks saved, and sorts`() =
        runTest {
            val fetchedProducts = listOf(product1, product2)
            val savedProducts = listOf(product2)

            whenever(productRepository.fetchProducts(10, 1)).thenReturn(fetchedProducts)
            whenever(productRepository.getFavoriteProducts()).thenReturn(savedProducts)

            val result = fetchProductsUseCase.invoke(sortOption = SortOption.PRICE_HIGH_TO_LOW, limit = 10, page = 1)

            Mockito.verify(productRepository).fetchProducts(limit = 10, page = 1)
            Mockito.verify(productRepository).getFavoriteProducts()
            assertThat(result).containsExactly(product1, product2).inOrder()
            assertThat(result.getOrNull(index = 0)?.isSaved).isTrue()
        }

    @Test
    fun `invoke returns empty list when no products fetched`() =
        runTest {
            whenever(productRepository.fetchProducts(limit = anyInt(), page = anyInt())).thenReturn(emptyList())

            val result = fetchProductsUseCase.invoke(sortOption = SortOption.NEW_TO_OLD, limit = 10, page = 1)

            assertThat(result).isEmpty()
        }
}