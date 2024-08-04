package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugurguner.productsapicasestudy.domain.useCase.ProductUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val productUseCases: ProductUseCases
) : ViewModel() {
    private val _badgeCount = MutableLiveData(0)
    val badgeCount: LiveData<Int> = _badgeCount

    fun updateBadgeCountAtStart() {
        viewModelScope.launch {
            val savedProductCount = productUseCases.getSavedProductsUseCase.invoke().count()
            _badgeCount.value = savedProductCount
        }
    }

    fun updateBadgeCountAfterSaveRemoveOperation(count: Int) {
        _badgeCount.value = count
    }
}