package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugurguner.productsapicasestudy.core.AppEvents
import com.eugurguner.productsapicasestudy.core.UIState
import com.eugurguner.productsapicasestudy.core.sortAndFilter.SortOption
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
class HomeFragmentViewModel @Inject constructor(
    private val productUseCases: ProductUseCases,
    private val cartProductUseCases: CartProductUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<List<Product>>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _appEvents: MutableStateFlow<AppEvents> = MutableStateFlow(AppEvents.None)
    val appEvents = _appEvents.asStateFlow()

    private val productList = mutableListOf<Product>()
    private var page = 0
    private var limit: MutableStateFlow<Int> = MutableStateFlow(20)
    var isLoading = false
    var hasMore = true

    private var sortOption: MutableStateFlow<SortOption> = MutableStateFlow(SortOption.OLD_TO_NEW)

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        if (isLoading || !hasMore) return

        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    productUseCases.fetchProductsUseCase.invoke(
                        sortOption = sortOption.value,
                        limit = limit.value,
                        page = ++page
                    )
                if (response.isEmpty()) {
                    hasMore = false
                    if (productList.isEmpty()) {
                        _uiState.value = UIState.Empty
                    } else {
                        _uiState.value = UIState.Success(emptyList())
                    }
                } else {
                    productList.addAll(response)
                    _uiState.value = UIState.Success(response)
                }
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "An unknown error occurred")
            } finally {
                isLoading = false
            }
        }
    }

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

    fun updateSortOption(sortOption: SortOption) {
        this.sortOption.update { sortOption }
    }

    fun getFetchedProducts(): List<Product> = productList

    fun onEventHandled() {
        _appEvents.update { AppEvents.None }
    }

    fun refreshProducts() {
        _uiState.value = UIState.Loading
        productList.clear()
        page = 0
        hasMore = true
        isLoading = false
        fetchProducts()
    }
}