package com.eugurguner.productsapicasestudy.domain.cart

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.cart.GetCartProductsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.Test

class GetCartProductsUseCaseTest {
    @Mock
    private lateinit var productRepository: ProductRepository

    private lateinit var getCartProductsUseCase: GetCartProductsUseCase

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
        getCartProductsUseCase = GetCartProductsUseCase(productRepository)
    }

    @Test
    fun `invoke retrieves cart products from repository`() =
        runTest {
            val cartProducts = listOf(product1, product2)
            whenever(productRepository.getCartProducts()).thenReturn(cartProducts)

            val result = getCartProductsUseCase()

            Mockito.verify(productRepository).getCartProducts()
            assertThat(result).isEqualTo(cartProducts)
        }
}