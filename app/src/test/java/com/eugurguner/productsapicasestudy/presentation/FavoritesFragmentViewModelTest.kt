package com.eugurguner.productsapicasestudy.presentation

import app.cash.turbine.test
import com.eugurguner.productsapicasestudy.core.AppEvents
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.domain.model.Product
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
import com.eugurguner.productsapicasestudy.presentation.viewModels.FavoritesFragmentViewModel
import com.eugurguner.productsapicasestudy.rules.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.Test

class FavoritesFragmentViewModelTest {
    private lateinit var productUseCases: ProductUseCases
    private lateinit var cartProductUseCases: CartProductUseCases
    private lateinit var viewModel: FavoritesFragmentViewModel

    @Mock
    private lateinit var repository: ProductRepository

    @get:Rule
    val coroutineRule = MainDispatcherRule()

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
        viewModel = FavoritesFragmentViewModel(productUseCases = productUseCases, cartProductUseCases = cartProductUseCases, dispatcher = coroutineRule.testDispatcher)
    }

    @Test
    fun `getFavoriteProducts should emit Loading and then Success with products`() =
        runTest {
            // Arrange
            val products =
                listOf(
                    product1,
                    product2
                )
            whenever(repository.getFavoriteProducts()).thenReturn(products)
            // Act & Assert
            viewModel.uiState.test {
                viewModel.getFavoriteProducts()
                assertThat(awaitItem()).isEqualTo(UIState.Loading)
                assertThat(awaitItem()).isEqualTo(UIState.Success(products))
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `getFavoriteProducts should emit Loading and then Empty with empty list`() =
        runTest {
            whenever(repository.getFavoriteProducts()).thenReturn(emptyList())
            viewModel.uiState.test {
                viewModel.getFavoriteProducts()
                assertThat(awaitItem()).isEqualTo(UIState.Loading)
                assertThat(awaitItem()).isEqualTo(UIState.Empty)
                cancelAndIgnoreRemainingEvents()
            }
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