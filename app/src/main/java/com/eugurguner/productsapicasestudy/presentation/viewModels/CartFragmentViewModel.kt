package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.domain.model.Product
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
class CartFragmentViewModel @Inject constructor(
    private val cartProductUseCases: CartProductUseCases,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<List<Product>>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getCartProducts() {
        viewModelScope.launch(dispatcher) {
            _uiState.value = UIState.Loading
            try {
                val products = cartProductUseCases.getCartProductsUseCase.invoke()
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

    fun decreaseProductQuantity(product: Product) {
        viewModelScope.launch(dispatcher) {
            cartProductUseCases.decreaseCartProductUseCase.invoke(product = product)
            val cartProducts = cartProductUseCases.getCartProductsUseCase.invoke()
            getCartProducts()
            val totalCount = cartProducts.sumOf { it.quantity }
            if (totalCount == 0) {
                _uiState.update { UIState.Empty }
            }
        }
    }

    fun increaseProductQuantity(product: Product) {
        viewModelScope.launch(dispatcher) {
            cartProductUseCases.increaseCartProductUseCase.invoke(product = product)
            getCartProducts()
        }
    }
}