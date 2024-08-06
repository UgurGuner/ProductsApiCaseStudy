package com.eugurguner.productsapicasestudy.presentation

import app.cash.turbine.test
import com.eugurguner.productsapicasestudy.core.AppEvents
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.core.sortAndFilter.SortOption
import com.eugurguner.productsapicasestudy.core.sortAndFilter.sortList
import com.eugurguner.productsapicasestudy.domain.fake.FakeProductRepository
import com.eugurguner.productsapicasestudy.domain.model.Product
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
import com.eugurguner.productsapicasestudy.presentation.viewModels.HomeFragmentViewModel
import com.eugurguner.productsapicasestudy.rules.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.mockito.kotlin.mock
import kotlin.test.Test

class HomeFragmentViewModelTest {
    private lateinit var productUseCases: ProductUseCases
    private lateinit var cartProductUseCases: CartProductUseCases
    private lateinit var viewModel: HomeFragmentViewModel

    private lateinit var fakeProductRepository: FakeProductRepository

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
        fakeProductRepository = FakeProductRepository()
        productUseCases =
            ProductUseCases(
                fetchProductsUseCase = FetchProductsUseCase(fakeProductRepository),
                getSavedProductsUseCase = GetSavedProductsUseCase(fakeProductRepository),
                saveProductUseCase = SaveProductUseCase(fakeProductRepository),
                removeProductUseCase = RemoveProductUseCase(fakeProductRepository)
            )
        cartProductUseCases =
            CartProductUseCases(
                getCartProductsUseCase = GetCartProductsUseCase(fakeProductRepository),
                addProductToCartUseCase = AddProductToCartUseCase(fakeProductRepository),
                decreaseCartProductUseCase = DecreaseCartProductUseCase(fakeProductRepository),
                increaseCartProductUseCase = IncreaseCartProductUseCase(fakeProductRepository)
            )
        viewModel = HomeFragmentViewModel(productUseCases = productUseCases, cartProductUseCases = cartProductUseCases, dispatcher = coroutineRule.testDispatcher)
    }

    @Test
    fun `fetchProducts should emit Loading and then Success with products`() =
        runTest {
            val initialList = listOf(product1, product2)
            val expectedList = sortList(sortOption = SortOption.OLD_TO_NEW, productList = initialList)
            viewModel.uiState.test {
                fakeProductRepository.emitAllProducts(initialList)
                fakeProductRepository.emitFavoriteProducts(emptyList())
                assertThat(awaitItem()).isEqualTo(UIState.Loading)
                assertThat(awaitItem()).isEqualTo(UIState.Success(expectedList))
            }
        }

    @Test
    fun `fetchProducts should emit Loading and then Empty with empty list`() =
        runTest {
            viewModel.uiState.test {
                fakeProductRepository.emitAllProducts(emptyList())
                fakeProductRepository.emitFavoriteProducts(emptyList())
                assertThat(awaitItem()).isEqualTo(UIState.Loading)
                assertThat(awaitItem()).isEqualTo(UIState.Empty)
            }
        }

    @Test
    fun `events handled on saveOrRemoveProduct function call`() =
        runTest {
            viewModel.appEvents.test {
                assertThat(awaitItem()).isEqualTo(AppEvents.None)
                viewModel.saveOrRemoveProduct(mock())
                fakeProductRepository.emitIsProcessCompleted()
                assertThat(awaitItem()).isEqualTo(AppEvents.OnFavoriteBadgeUpdate)
            }
        }

    @Test
    fun `events handled on addProductToCart function call`() =
        runTest {
            viewModel.appEvents.test {
                assertThat(awaitItem()).isEqualTo(AppEvents.None)
                viewModel.addProductToCart(mock())
                fakeProductRepository.emitCardProductById(mock())
                fakeProductRepository.emitIsProcessCompleted()
                assertThat(awaitItem()).isEqualTo(AppEvents.OnCartBadgeUpdate)
            }
        }

    @Test
    fun `events handled on None function call`() =
        runTest {
            viewModel.appEvents.test {
                assertThat(awaitItem()).isEqualTo(AppEvents.None)
                viewModel.saveOrRemoveProduct(mock())
                fakeProductRepository.emitIsProcessCompleted()
                assertThat(awaitItem()).isEqualTo(AppEvents.OnFavoriteBadgeUpdate)
                viewModel.onEventHandled()
                fakeProductRepository.emitIsProcessCompleted()
                assertThat(awaitItem()).isEqualTo(AppEvents.None)
            }
        }
}