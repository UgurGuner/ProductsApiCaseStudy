package com.eugurguner.productsapicasestudy.domain.cart

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.cart.IncreaseCartProductUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.Test

class IncreaseCartProductUseCaseTest {
    @Mock
    private lateinit var mockProductRepository: ProductRepository

    private lateinit var increaseCartProductUseCase: IncreaseCartProductUseCase

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
        increaseCartProductUseCase = IncreaseCartProductUseCase(mockProductRepository)
    }

    @Test
    fun `invoke should increase quantity when product quantity is greater than or equal to one`() =
        runTest {
            increaseCartProductUseCase.invoke(product)
            Mockito.verify(mockProductRepository).increaseCartProductQuantity(product)
        }

    @Test
    fun `invoke should add product to cart when product quantity is less than one`() =
        runTest {
            val productQ1 = product.copy(quantity = 0)
            increaseCartProductUseCase.invoke(productQ1)
            Mockito.verify(mockProductRepository).addProductToCart(productQ1)
        }
}