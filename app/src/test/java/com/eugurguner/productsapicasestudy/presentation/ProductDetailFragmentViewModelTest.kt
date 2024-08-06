package com.eugurguner.productsapicasestudy.presentation

import app.cash.turbine.test
import com.eugurguner.productsapicasestudy.core.AppEvents
import com.eugurguner.productsapicasestudy.domain.repository.ProductRepository
import com.eugurguner.productsapicasestudy.domain.useCase.FetchProductsUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.GetSavedProductsUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.ProductUseCases
import com.eugurguner.productsapicasestudy.domain.useCase.RemoveProductUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.SaveProductUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.AddProductToCartUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.CartProductUseCases
import com.eugurguner.productsapicasestudy.domain.useCase.cart.DecreaseCartProductUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.GetCartProductsUseCase
import com.eugurguner.productsapicasestudy.domain.useCase.cart.IncreaseCartProductUseCase
import com.eugurguner.productsapicasestudy.presentation.viewModels.ProductDetailFragmentViewModel
import com.eugurguner.productsapicasestudy.rules.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import kotlin.test.Test

class ProductDetailFragmentViewModelTest {
    private lateinit var productUseCases: ProductUseCases
    private lateinit var cartProductUseCases: CartProductUseCases
    private lateinit var viewModel: ProductDetailFragmentViewModel

    @Mock
    private lateinit var repository: ProductRepository

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        productUseCases =
            ProductUseCases(
                fetchProductsUseCase = FetchProductsUseCase(repository),
                getSavedProductsUseCase = GetSavedProductsUseCase(repository),
                saveProductUseCase = SaveProductUseCase(repository),
                removeProductUseCase = RemoveProductUseCase(repository)
            )
        cartProductUseCases =
            CartProductUseCases(
                getCartProductsUseCase = GetCartProductsUseCase(repository),
                addProductToCartUseCase = AddProductToCartUseCase(repository),
                decreaseCartProductUseCase = DecreaseCartProductUseCase(repository),
                increaseCartProductUseCase = IncreaseCartProductUseCase(repository)
            )
        viewModel =
            ProductDetailFragmentViewModel(
                productUseCases = productUseCases,
                cartProductUseCases = cartProductUseCases,
                dispatcher = coroutineRule.testDispatcher
            )
    }

    @Test
    fun `events handled on saveOrRemoveProduct function call`() =
        runTest {
            viewModel.appEvents.test {
                assertThat(awaitItem()).isEqualTo(AppEvents.None)
                viewModel.saveOrRemoveProduct(mock())
                assertThat(awaitItem()).isEqualTo(AppEvents.OnFavoriteBadgeUpdate)
            }
        }

    @Test
    fun `events handled on addProductToCart function call`() =
        runTest {
            viewModel.appEvents.test {
                assertThat(awaitItem()).isEqualTo(AppEvents.None)
                viewModel.addProductToCart(mock())
                assertThat(awaitItem()).isEqualTo(AppEvents.OnCartBadgeUpdate)
            }
        }

    @Test
    fun `events handled on None function call`() =
        runTest {
            viewModel.appEvents.test {
                assertThat(awaitItem()).isEqualTo(AppEvents.None)
                viewModel.saveOrRemoveProduct(mock())
                assertThat(awaitItem()).isEqualTo(AppEvents.OnFavoriteBadgeUpdate)
                viewModel.onEventHandled()
                assertThat(awaitItem()).isEqualTo(AppEvents.None)
            }
        }
}