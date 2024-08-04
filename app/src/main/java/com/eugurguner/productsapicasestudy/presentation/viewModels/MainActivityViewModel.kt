package com.eugurguner.productsapicasestudy.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    private val _badgeCount = MutableLiveData(0)
    val badgeCount: LiveData<Int> = _badgeCount

    fun updateBadgeCount(newCount: Int) {
        _badgeCount.value = newCount
    }
}