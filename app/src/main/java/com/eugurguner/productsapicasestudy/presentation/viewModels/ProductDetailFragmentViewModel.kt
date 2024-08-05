package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _favoriteBadgeCount: MutableStateFlow<Int?> = MutableStateFlow(null)
    val favoriteBadgeCount = _favoriteBadgeCount.asStateFlow()

    private val _cartBadgeCount: MutableStateFlow<Int?> = MutableStateFlow(null)
    val cartBadgeCount = _cartBadgeCount.asStateFlow()

    fun saveOrRemoveProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!product.isSaved) {
                productUseCases.removeProductUseCase.invoke(productId = product.id)
            } else {
                productUseCases.saveProductUseCase.invoke(product = product)
            }
            val savedProductCount = productUseCases.getSavedProductsUseCase.invoke().count()
            _favoriteBadgeCount.update { savedProductCount }
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            cartProductUseCases.addProductToCartUseCase.invoke(product = product)
            val cartProducts = cartProductUseCases.getCartProductsUseCase.invoke()
            _cartBadgeCount.update { cartProducts.sumOf { it.quantitiy } }
        }
    }
}