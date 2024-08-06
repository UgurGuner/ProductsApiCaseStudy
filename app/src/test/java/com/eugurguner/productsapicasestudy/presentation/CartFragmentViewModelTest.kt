package com.eugurguner.productsapicasestudy.presentation

import app.cash.turbine.test
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.domain.fake.FakeProductRepository
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.useCase.cart.AddProductToCartUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.CartProductUseCases
import com.eugurguner.productsapicasestudy.domain.useCase.cart.DecreaseCartProductUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.GetCartProductsUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.IncreaseCartProductUseCase
import com.eugurguner.productsapicasestudy.presentation.viewModels.CartFragmentViewModel
import com.eugurguner.productsapicasestudy.rules.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class CartFragmentViewModelTest {
    @get:Rule
    val testDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: CartFragmentViewModel
    private lateinit var cartProductUseCases: CartProductUseCases
    private lateinit var fakeProductRepository: FakeProductRepository

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
        fakeProductRepository = FakeProductRepository()
        cartProductUseCases =
            CartProductUseCases(
                getCartProductsUseCase = GetCartProductsUseCase(fakeProductRepository),
                addProductToCartUseCase = AddProductToCartUseCase(fakeProductRepository),
                increaseCartProductUseCase = IncreaseCartProductUseCase(fakeProductRepository),
                decreaseCartProductUseCase = DecreaseCartProductUseCase(fakeProductRepository)
            )
        viewModel = CartFragmentViewModel(cartProductUseCases = cartProductUseCases, dispatcher = testDispatcherRule.testDispatcher)
    }

    @Test
    fun `getCartProducts returns mocked list with state change correctly`() =
        runTest {
            val initialList = listOf(product1, product2)
            viewModel.uiState.test {
                viewModel.getCartProducts()
                fakeProductRepository.emitCartList(initialList)
                assertThat(awaitItem()).isEqualTo(UIState.Loading)
                assertThat(awaitItem()).isEqualTo(UIState.Success(initialList))
            }
        }

    @Test
    fun `getCartProducts returns empty list with state change correctly`() =
        runTest {
            viewModel.uiState.test {
                assertThat(awaitItem()).isEqualTo(UIState.Loading)
                viewModel.getCartProducts()
                fakeProductRepository.emitCartList(emptyList())
                assertThat(awaitItem()).isEqualTo(UIState.Empty)
            }
        }
}