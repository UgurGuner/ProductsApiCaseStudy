package com.eugurguner.productsapicasestudy.domain.product

import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.RemoveProductUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.Test

@RunWith(MockitoJUnitRunner::class)
class RemoveProductUseCaseTest {
    @Mock
    private lateinit var productRepository: ProductRepository

    private lateinit var removeProductUseCase: RemoveProductUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        removeProductUseCase = RemoveProductUseCase(productRepository)
    }

    @Test
    fun `invoke calls removeProduct on repository`() =
        runTest {
            val productId = "1"
            removeProductUseCase(productId)
            Mockito.verify(productRepository).removeProduct(productId)
        }
}