package com.eugurguner.productsapicasestudy.domain.cart

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.cart.DecreaseCartProductUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import kotlin.test.Test

class DecreaseCartProductUseCaseTest {
    @Mock
    private lateinit var mockProductRepository: ProductRepository

    private lateinit var decreaseCartProductUseCase: DecreaseCartProductUseCase

    private val product =
        Product(
            id = "1",
            name = "Product A",
            imageUrl = "image_url",
            price = 10.0,
            description = "Description",
            model = "Model A",
            brand = "Brand A",
            createdAt = "2024-08-05T14:52:00Z",
            isSaved = true,
            quantity = 2
        )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        decreaseCartProductUseCase = DecreaseCartProductUseCase(mockProductRepository)
    }

    @Test
    fun `invoke should remove product from the cart since quantity is one`() =
        runTest {
            val productQ1 = product.copy(quantity = 1)
            decreaseCartProductUseCase.invoke(productQ1)
            Mockito.verify(mockProductRepository).removeProductFromCart(productId = productQ1.id)
        }

    @Test
    fun `invoke should decrease quantity when product quantity is greater than one`() =
        runTest {
            decreaseCartProductUseCase.invoke(product)
            val captor = argumentCaptor<Product>()
            Mockito.verify(mockProductRepository).decreaseCartProductQuantity(captor.capture())
            assertThat(captor.firstValue).isEqualTo(product)
        }

    @Test
    fun `invoke should remove product from cart when product quantity is one`() =
        runTest {
            val productQ1 = product.copy(quantity = 1)
            decreaseCartProductUseCase.invoke(productQ1)
            Mockito.verify(mockProductRepository).removeProductFromCart(productQ1.id)
        }
}