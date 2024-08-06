package com.eugurguner.productsapicasestudy.domain.product

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.SaveProductUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import kotlin.test.Test

class SaveProductUseCaseTest {
    @Mock
    private lateinit var mockProductRepository: ProductRepository

    private lateinit var saveProductUseCase: SaveProductUseCase

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

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        saveProductUseCase = SaveProductUseCase(mockProductRepository)
    }

    @Test
    fun `invoke should call saveProduct on repository`() =
        runTest {
            saveProductUseCase.invoke(product1)

            val captor = argumentCaptor<Product>()
            Mockito.verify(mockProductRepository).saveProduct(captor.capture())
            assertThat(captor.firstValue).isEqualTo(product1)
        }
}