package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugurguner.productsapicasestudy.domain.useCase.ProductUseCases
import com.eugurguner.productsapicasestudy.domain.useCase.cart.CartProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val productUseCases: ProductUseCases,
    private val cartProductUseCases: CartProductUseCases
) : ViewModel() {
    private val _favoriteBadgeCount = MutableLiveData(0)
    val favoriteBadgeCount: LiveData<Int> = _favoriteBadgeCount

    private val _cartBadgeCount = MutableLiveData(0)
    val cartBadgeCount: LiveData<Int> = _cartBadgeCount

    fun updateFavoriteBadgeCountAtStart() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedProductCount = productUseCases.getSavedProductsUseCase.invoke().count()
            withContext(Dispatchers.Main) {
                _favoriteBadgeCount.value = savedProductCount
            }
        }
    }

    fun updateFavoriteBadgeCountAfterSaveRemoveOperation(count: Int) {
        _favoriteBadgeCount.value = count
    }

    fun updateCartBadgeCountAtStart() {
        viewModelScope.launch(Dispatchers.IO) {
            val cartProducts = cartProductUseCases.getCartProductsUseCase.invoke()
            withContext(Dispatchers.Main) {
                _cartBadgeCount.value = cartProducts.sumOf { it.quantitiy }
            }
        }
    }

    fun updateCartBadgeCountAfterSaveRemoveOperation(count: Int) {
        _cartBadgeCount.value = count
    }
}