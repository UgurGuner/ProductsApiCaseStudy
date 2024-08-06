package com.eugurguner.productsapicasestudy.domain.cart

import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.cart.AddProductToCartUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import kotlin.test.Test

class AddProductToCartUseCaseTest {
    @Mock
    private lateinit var mockProductRepository: ProductRepository

    private lateinit var addProductToCartUseCase: AddProductToCartUseCase

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
            isSaved = true
        )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        addProductToCartUseCase = AddProductToCartUseCase(mockProductRepository)
    }

    @Test
    fun `invoke should increase quantity when product is already in cart`() =
        runTest {
            whenever(mockProductRepository.getCartProductById(product.id)).thenReturn(product)
            addProductToCartUseCase.invoke(product)
            val captor = argumentCaptor<Product>()
            Mockito.verify(mockProductRepository).increaseCartProductQuantity(captor.capture())
            assertThat(captor.firstValue).isEqualTo(product)
        }

    @Test
    fun `invoke should add product to cart when product is not in cart`() =
        runTest {
            Mockito.`when`(mockProductRepository.getCartProductById(product.id)).thenReturn(null)
            addProductToCartUseCase.invoke(product)
            val captor = argumentCaptor<Product>()
            Mockito.verify(mockProductRepository).addProductToCart(captor.capture())
            assertThat(captor.firstValue).isEqualTo(product)
        }
}