package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugurguner.productsapicasestudy.core.AppEvents
import com.eugurguner.productsapicasestudy.domain.model.Product
import com.eugurguner.productsapicasestudy.domain.useCase.ProductUseCases
import com.eugurguner.productsapicasestudy.domain.useCase.cart.CartProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailFragmentViewModel @Inject constructor(
    private val productUseCases: ProductUseCases,
    private val cartProductUseCases: CartProductUseCases
) : ViewModel() {
    private val _appEvents: MutableStateFlow<AppEvents> = MutableStateFlow(AppEvents.None)
    val appEvents = _appEvents.asStateFlow()

    fun saveOrRemoveProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!product.isSaved) {
                productUseCases.removeProductUseCase.invoke(productId = product.id)
            } else {
                productUseCases.saveProductUseCase.invoke(product = product)
            }
            _appEvents.update { AppEvents.OnFavoriteBadgeUpdate }
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            cartProductUseCases.addProductToCartUseCase.invoke(product = product)
            _appEvents.update { AppEvents.OnCartBadgeUpdate }
        }
    }

    fun onEventHandled() {
        _appEvents.update { AppEvents.None }
    }
}