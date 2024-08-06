package com.eugurguner.productsapicasestudy.domain.product

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.GetSavedProductsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import kotlin.test.Test

@RunWith(MockitoJUnitRunner::class)
class GetSavedProductsUseCaseTest {
    @Mock
    private lateinit var productRepository: ProductRepository

    private lateinit var getSavedProductsUseCase: GetSavedProductsUseCase

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

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getSavedProductsUseCase = GetSavedProductsUseCase(productRepository)
    }

    @Test
    fun `invoke retrieves saved products and marks them as saved`() =
        runTest {
            val savedProducts = listOf(product1, product2)

            whenever(productRepository.getFavoriteProducts()).thenReturn(savedProducts)

            val result = getSavedProductsUseCase.invoke()

            Mockito.verify(productRepository).getFavoriteProducts()
            assertThat(result).containsExactly(product1.copy(isSaved = true), product2.copy(isSaved = true))
        }

    @Test
    fun `invoke returns empty list when no saved products`() =
        runTest {
            whenever(productRepository.getFavoriteProducts()).thenReturn(emptyList())

            val result = getSavedProductsUseCase.invoke()

            assertThat(result).isEmpty()
        }
}