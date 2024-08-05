package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.useCase.cart.CartProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartFragmentViewModel @Inject constructor(
    private val cartProductUseCases: CartProductUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<List<Product>>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    fun getCartProducts() {
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            cartProductUseCases.decreaseCartProductUseCase.invoke(product = product)
            getCartProducts()
            val cartProducts = cartProductUseCases.getCartProductsUseCase.invoke()
            val totalCount = cartProducts.sumOf { it.quantitiy }
            if (totalCount == 0) {
                _uiState.update { UIState.Empty }
            }
        }
    }

    fun increaseProductQuantity(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            cartProductUseCases.increaseCartProductUseCase.invoke(product = product)
            getCartProducts()
        }
    }
}