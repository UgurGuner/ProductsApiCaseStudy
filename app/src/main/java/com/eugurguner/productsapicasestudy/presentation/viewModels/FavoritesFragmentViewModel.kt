package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugurguner.productsapicasestudy.core.AppEvents
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.useCase.ProductUseCases
import com.eugurguner.productsapicasestudy.domain.useCase.cart.CartProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FavoritesFragmentViewModel @Inject constructor(
    private val productUseCases: ProductUseCases,
    private val cartProductUseCases: CartProductUseCases,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<List<Product>>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _appEvents: MutableStateFlow<AppEvents> = MutableStateFlow(AppEvents.None)
    val appEvents = _appEvents.asStateFlow()

    fun getFavoriteProducts() {
        viewModelScope.launch(dispatcher) {
            _uiState.value = UIState.Loading
            try {
                val products = productUseCases.getSavedProductsUseCase.invoke()
                if (products.isEmpty()) {
                    _uiState.value = UIState.Empty
                } else {
                    _uiState.value = UIState.Success(products)
                }
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun saveOrRemoveProduct(product: Product) {
        viewModelScope.launch(dispatcher) {
            if (!product.isSaved) {
                productUseCases.removeProductUseCase.invoke(productId = product.id)
            } else {
                productUseCases.saveProductUseCase.invoke(product = product)
            }
            _appEvents.update { AppEvents.OnFavoriteBadgeUpdate }
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch(dispatcher) {
            cartProductUseCases.addProductToCartUseCase.invoke(product = product)
            _appEvents.update { AppEvents.OnCartBadgeUpdate }
        }
    }

    fun onEventHandled() {
        _appEvents.update { AppEvents.None }
    }
}